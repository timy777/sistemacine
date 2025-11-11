package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sistemacine.IntegrationTest;
import sistemacine.domain.Sala;
import sistemacine.domain.enumeration.TipoSala;
import sistemacine.repository.SalaRepository;
import sistemacine.service.dto.SalaDTO;
import sistemacine.service.mapper.SalaMapper;

/**
 * Integration tests for the {@link SalaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SalaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDAD = 1;
    private static final Integer UPDATED_CAPACIDAD = 2;

    private static final TipoSala DEFAULT_TIPO = TipoSala.DOSD;
    private static final TipoSala UPDATED_TIPO = TipoSala.TRESD;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private SalaMapper salaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Sala sala;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sala createEntity() {
        Sala sala = new Sala().nombre(DEFAULT_NOMBRE).capacidad(DEFAULT_CAPACIDAD).tipo(DEFAULT_TIPO).estado(DEFAULT_ESTADO);
        return sala;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sala createUpdatedEntity() {
        Sala sala = new Sala().nombre(UPDATED_NOMBRE).capacidad(UPDATED_CAPACIDAD).tipo(UPDATED_TIPO).estado(UPDATED_ESTADO);
        return sala;
    }

    @BeforeEach
    public void initTest() {
        salaRepository.deleteAll().block();
        sala = createEntity();
    }

    @Test
    void createSala() throws Exception {
        int databaseSizeBeforeCreate = salaRepository.findAll().collectList().block().size();
        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate + 1);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSala.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testSala.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testSala.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createSalaWithExistingId() throws Exception {
        // Create the Sala with an existing ID
        sala.setId("existing_id");
        SalaDTO salaDTO = salaMapper.toDto(sala);

        int databaseSizeBeforeCreate = salaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaRepository.findAll().collectList().block().size();
        // set the field null
        sala.setNombre(null);

        // Create the Sala, which fails.
        SalaDTO salaDTO = salaMapper.toDto(sala);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCapacidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaRepository.findAll().collectList().block().size();
        // set the field null
        sala.setCapacidad(null);

        // Create the Sala, which fails.
        SalaDTO salaDTO = salaMapper.toDto(sala);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaRepository.findAll().collectList().block().size();
        // set the field null
        sala.setTipo(null);

        // Create the Sala, which fails.
        SalaDTO salaDTO = salaMapper.toDto(sala);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSalas() {
        // Initialize the database
        salaRepository.save(sala).block();

        // Get all the salaList
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
            .value(hasItem(sala.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].capacidad")
            .value(hasItem(DEFAULT_CAPACIDAD))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO.toString()))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO));
    }

    @Test
    void getSala() {
        // Initialize the database
        salaRepository.save(sala).block();

        // Get the sala
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sala.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sala.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.capacidad")
            .value(is(DEFAULT_CAPACIDAD))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO.toString()))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO));
    }

    @Test
    void getNonExistingSala() {
        // Get the sala
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSala() throws Exception {
        // Initialize the database
        salaRepository.save(sala).block();

        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();

        // Update the sala
        Sala updatedSala = salaRepository.findById(sala.getId()).block();
        updatedSala.nombre(UPDATED_NOMBRE).capacidad(UPDATED_CAPACIDAD).tipo(UPDATED_TIPO).estado(UPDATED_ESTADO);
        SalaDTO salaDTO = salaMapper.toDto(updatedSala);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSala.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testSala.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testSala.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, salaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSalaWithPatch() throws Exception {
        // Initialize the database
        salaRepository.save(sala).block();

        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();

        // Update the sala using partial update
        Sala partialUpdatedSala = new Sala();
        partialUpdatedSala.setId(sala.getId());

        partialUpdatedSala.estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSala.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSala))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSala.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testSala.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testSala.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void fullUpdateSalaWithPatch() throws Exception {
        // Initialize the database
        salaRepository.save(sala).block();

        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();

        // Update the sala using partial update
        Sala partialUpdatedSala = new Sala();
        partialUpdatedSala.setId(sala.getId());

        partialUpdatedSala.nombre(UPDATED_NOMBRE).capacidad(UPDATED_CAPACIDAD).tipo(UPDATED_TIPO).estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSala.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSala))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
        Sala testSala = salaList.get(salaList.size() - 1);
        assertThat(testSala.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSala.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testSala.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testSala.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, salaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSala() throws Exception {
        int databaseSizeBeforeUpdate = salaRepository.findAll().collectList().block().size();
        sala.setId(UUID.randomUUID().toString());

        // Create the Sala
        SalaDTO salaDTO = salaMapper.toDto(sala);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(salaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sala in the database
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSala() {
        // Initialize the database
        salaRepository.save(sala).block();

        int databaseSizeBeforeDelete = salaRepository.findAll().collectList().block().size();

        // Delete the sala
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sala.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sala> salaList = salaRepository.findAll().collectList().block();
        assertThat(salaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
