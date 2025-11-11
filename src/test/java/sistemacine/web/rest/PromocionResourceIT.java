package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.IntegrationTest;
import sistemacine.domain.Promocion;
import sistemacine.repository.PromocionRepository;
import sistemacine.service.PromocionService;
import sistemacine.service.dto.PromocionDTO;
import sistemacine.service.mapper.PromocionMapper;

/**
 * Integration tests for the {@link PromocionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PromocionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Double DEFAULT_PORCENTAJE_DESCUENTO = 1D;
    private static final Double UPDATED_PORCENTAJE_DESCUENTO = 2D;

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String ENTITY_API_URL = "/api/promocions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PromocionRepository promocionRepository;

    @Mock
    private PromocionRepository promocionRepositoryMock;

    @Autowired
    private PromocionMapper promocionMapper;

    @Mock
    private PromocionService promocionServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Promocion promocion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createEntity() {
        Promocion promocion = new Promocion()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .porcentajeDescuento(DEFAULT_PORCENTAJE_DESCUENTO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .activa(DEFAULT_ACTIVA);
        return promocion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promocion createUpdatedEntity() {
        Promocion promocion = new Promocion()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activa(UPDATED_ACTIVA);
        return promocion;
    }

    @BeforeEach
    public void initTest() {
        promocionRepository.deleteAll().block();
        promocion = createEntity();
    }

    @Test
    void createPromocion() throws Exception {
        int databaseSizeBeforeCreate = promocionRepository.findAll().collectList().block().size();
        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate + 1);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(DEFAULT_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getActiva()).isEqualTo(DEFAULT_ACTIVA);
    }

    @Test
    void createPromocionWithExistingId() throws Exception {
        // Create the Promocion with an existing ID
        promocion.setId("existing_id");
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        int databaseSizeBeforeCreate = promocionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().collectList().block().size();
        // set the field null
        promocion.setNombre(null);

        // Create the Promocion, which fails.
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPorcentajeDescuentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().collectList().block().size();
        // set the field null
        promocion.setPorcentajeDescuento(null);

        // Create the Promocion, which fails.
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().collectList().block().size();
        // set the field null
        promocion.setFechaInicio(null);

        // Create the Promocion, which fails.
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().collectList().block().size();
        // set the field null
        promocion.setFechaFin(null);

        // Create the Promocion, which fails.
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkActivaIsRequired() throws Exception {
        int databaseSizeBeforeTest = promocionRepository.findAll().collectList().block().size();
        // set the field null
        promocion.setActiva(null);

        // Create the Promocion, which fails.
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPromocions() {
        // Initialize the database
        promocionRepository.save(promocion).block();

        // Get all the promocionList
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
            .value(hasItem(promocion.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION))
            .jsonPath("$.[*].porcentajeDescuento")
            .value(hasItem(DEFAULT_PORCENTAJE_DESCUENTO.doubleValue()))
            .jsonPath("$.[*].fechaInicio")
            .value(hasItem(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.[*].fechaFin")
            .value(hasItem(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.[*].activa")
            .value(hasItem(DEFAULT_ACTIVA.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsEnabled() {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(promocionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPromocionsWithEagerRelationshipsIsNotEnabled() {
        when(promocionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(promocionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPromocion() {
        // Initialize the database
        promocionRepository.save(promocion).block();

        // Get the promocion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, promocion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(promocion.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION))
            .jsonPath("$.porcentajeDescuento")
            .value(is(DEFAULT_PORCENTAJE_DESCUENTO.doubleValue()))
            .jsonPath("$.fechaInicio")
            .value(is(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.fechaFin")
            .value(is(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.activa")
            .value(is(DEFAULT_ACTIVA.booleanValue()));
    }

    @Test
    void getNonExistingPromocion() {
        // Get the promocion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPromocion() throws Exception {
        // Initialize the database
        promocionRepository.save(promocion).block();

        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();

        // Update the promocion
        Promocion updatedPromocion = promocionRepository.findById(promocion.getId()).block();
        updatedPromocion
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activa(UPDATED_ACTIVA);
        PromocionDTO promocionDTO = promocionMapper.toDto(updatedPromocion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, promocionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(UPDATED_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getActiva()).isEqualTo(UPDATED_ACTIVA);
    }

    @Test
    void putNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, promocionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.save(promocion).block();

        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion.nombre(UPDATED_NOMBRE).activa(UPDATED_ACTIVA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(DEFAULT_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
        assertThat(testPromocion.getActiva()).isEqualTo(UPDATED_ACTIVA);
    }

    @Test
    void fullUpdatePromocionWithPatch() throws Exception {
        // Initialize the database
        promocionRepository.save(promocion).block();

        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();

        // Update the promocion using partial update
        Promocion partialUpdatedPromocion = new Promocion();
        partialUpdatedPromocion.setId(promocion.getId());

        partialUpdatedPromocion
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .porcentajeDescuento(UPDATED_PORCENTAJE_DESCUENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activa(UPDATED_ACTIVA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromocion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromocion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
        Promocion testPromocion = promocionList.get(promocionList.size() - 1);
        assertThat(testPromocion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPromocion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPromocion.getPorcentajeDescuento()).isEqualTo(UPDATED_PORCENTAJE_DESCUENTO);
        assertThat(testPromocion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testPromocion.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
        assertThat(testPromocion.getActiva()).isEqualTo(UPDATED_ACTIVA);
    }

    @Test
    void patchNonExistingPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, promocionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPromocion() throws Exception {
        int databaseSizeBeforeUpdate = promocionRepository.findAll().collectList().block().size();
        promocion.setId(UUID.randomUUID().toString());

        // Create the Promocion
        PromocionDTO promocionDTO = promocionMapper.toDto(promocion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promocionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Promocion in the database
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePromocion() {
        // Initialize the database
        promocionRepository.save(promocion).block();

        int databaseSizeBeforeDelete = promocionRepository.findAll().collectList().block().size();

        // Delete the promocion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, promocion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Promocion> promocionList = promocionRepository.findAll().collectList().block();
        assertThat(promocionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
