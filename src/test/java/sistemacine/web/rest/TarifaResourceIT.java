package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static sistemacine.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sistemacine.IntegrationTest;
import sistemacine.domain.Tarifa;
import sistemacine.domain.enumeration.TipoSala;
import sistemacine.repository.EntityManager;
import sistemacine.repository.TarifaRepository;
import sistemacine.service.dto.TarifaDTO;
import sistemacine.service.mapper.TarifaMapper;

/**
 * Integration tests for the {@link TarifaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TarifaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(2);

    private static final String DEFAULT_DIA_SEMANA = "AAAAAAAAAA";
    private static final String UPDATED_DIA_SEMANA = "BBBBBBBBBB";

    private static final TipoSala DEFAULT_TIPO_SALA = TipoSala.DOSD;
    private static final TipoSala UPDATED_TIPO_SALA = TipoSala.TRESD;

    private static final String ENTITY_API_URL = "/api/tarifas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private TarifaMapper tarifaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Tarifa tarifa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarifa createEntity(EntityManager em) {
        Tarifa tarifa = new Tarifa()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .monto(DEFAULT_MONTO)
            .diaSemana(DEFAULT_DIA_SEMANA)
            .tipoSala(DEFAULT_TIPO_SALA);
        return tarifa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarifa createUpdatedEntity(EntityManager em) {
        Tarifa tarifa = new Tarifa()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .tipoSala(UPDATED_TIPO_SALA);
        return tarifa;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Tarifa.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        tarifa = createEntity(em);
    }

    @Test
    void createTarifa() throws Exception {
        int databaseSizeBeforeCreate = tarifaRepository.findAll().collectList().block().size();
        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeCreate + 1);
        Tarifa testTarifa = tarifaList.get(tarifaList.size() - 1);
        assertThat(testTarifa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTarifa.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTarifa.getMonto()).isEqualByComparingTo(DEFAULT_MONTO);
        assertThat(testTarifa.getDiaSemana()).isEqualTo(DEFAULT_DIA_SEMANA);
        assertThat(testTarifa.getTipoSala()).isEqualTo(DEFAULT_TIPO_SALA);
    }

    @Test
    void createTarifaWithExistingId() throws Exception {
        // Create the Tarifa with an existing ID
        tarifa.setId(1L);
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        int databaseSizeBeforeCreate = tarifaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarifaRepository.findAll().collectList().block().size();
        // set the field null
        tarifa.setNombre(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarifaRepository.findAll().collectList().block().size();
        // set the field null
        tarifa.setMonto(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTarifas() {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        // Get all the tarifaList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(tarifa.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].monto")
            .value(hasItem(sameNumber(DEFAULT_MONTO)))
            .jsonPath("$.[*].diaSemana")
            .value(hasItem(DEFAULT_DIA_SEMANA))
            .jsonPath("$.[*].tipoSala")
            .value(hasItem(DEFAULT_TIPO_SALA.toString()));
    }

    @Test
    void getTarifa() {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        // Get the tarifa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tarifa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tarifa.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.monto")
            .value(is(sameNumber(DEFAULT_MONTO)))
            .jsonPath("$.diaSemana")
            .value(is(DEFAULT_DIA_SEMANA))
            .jsonPath("$.tipoSala")
            .value(is(DEFAULT_TIPO_SALA.toString()));
    }

    @Test
    void getNonExistingTarifa() {
        // Get the tarifa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTarifa() throws Exception {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();

        // Update the tarifa
        Tarifa updatedTarifa = tarifaRepository.findById(tarifa.getId()).block();
        updatedTarifa
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .tipoSala(UPDATED_TIPO_SALA);
        TarifaDTO tarifaDTO = tarifaMapper.toDto(updatedTarifa);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tarifaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
        Tarifa testTarifa = tarifaList.get(tarifaList.size() - 1);
        assertThat(testTarifa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTarifa.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTarifa.getMonto()).isEqualByComparingTo(UPDATED_MONTO);
        assertThat(testTarifa.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testTarifa.getTipoSala()).isEqualTo(UPDATED_TIPO_SALA);
    }

    @Test
    void putNonExistingTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tarifaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTarifaWithPatch() throws Exception {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();

        // Update the tarifa using partial update
        Tarifa partialUpdatedTarifa = new Tarifa();
        partialUpdatedTarifa.setId(tarifa.getId());

        partialUpdatedTarifa.nombre(UPDATED_NOMBRE).diaSemana(UPDATED_DIA_SEMANA).tipoSala(UPDATED_TIPO_SALA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarifa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTarifa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
        Tarifa testTarifa = tarifaList.get(tarifaList.size() - 1);
        assertThat(testTarifa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTarifa.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTarifa.getMonto()).isEqualByComparingTo(DEFAULT_MONTO);
        assertThat(testTarifa.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testTarifa.getTipoSala()).isEqualTo(UPDATED_TIPO_SALA);
    }

    @Test
    void fullUpdateTarifaWithPatch() throws Exception {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();

        // Update the tarifa using partial update
        Tarifa partialUpdatedTarifa = new Tarifa();
        partialUpdatedTarifa.setId(tarifa.getId());

        partialUpdatedTarifa
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .tipoSala(UPDATED_TIPO_SALA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTarifa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTarifa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
        Tarifa testTarifa = tarifaList.get(tarifaList.size() - 1);
        assertThat(testTarifa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTarifa.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTarifa.getMonto()).isEqualByComparingTo(UPDATED_MONTO);
        assertThat(testTarifa.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testTarifa.getTipoSala()).isEqualTo(UPDATED_TIPO_SALA);
    }

    @Test
    void patchNonExistingTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tarifaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTarifa() throws Exception {
        int databaseSizeBeforeUpdate = tarifaRepository.findAll().collectList().block().size();
        tarifa.setId(count.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tarifaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tarifa in the database
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTarifa() {
        // Initialize the database
        tarifaRepository.save(tarifa).block();

        int databaseSizeBeforeDelete = tarifaRepository.findAll().collectList().block().size();

        // Delete the tarifa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tarifa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Tarifa> tarifaList = tarifaRepository.findAll().collectList().block();
        assertThat(tarifaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
