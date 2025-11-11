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
import sistemacine.domain.Reporte;
import sistemacine.repository.ReporteRepository;
import sistemacine.service.ReporteService;
import sistemacine.service.dto.ReporteDTO;
import sistemacine.service.mapper.ReporteMapper;

/**
 * Integration tests for the {@link ReporteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReporteResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_GENERACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_GENERACION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reportes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteRepository reporteRepositoryMock;

    @Autowired
    private ReporteMapper reporteMapper;

    @Mock
    private ReporteService reporteServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Reporte reporte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporte createEntity() {
        Reporte reporte = new Reporte().tipo(DEFAULT_TIPO).fechaGeneracion(DEFAULT_FECHA_GENERACION).descripcion(DEFAULT_DESCRIPCION);
        return reporte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reporte createUpdatedEntity() {
        Reporte reporte = new Reporte().tipo(UPDATED_TIPO).fechaGeneracion(UPDATED_FECHA_GENERACION).descripcion(UPDATED_DESCRIPCION);
        return reporte;
    }

    @BeforeEach
    public void initTest() {
        reporteRepository.deleteAll().block();
        reporte = createEntity();
    }

    @Test
    void createReporte() throws Exception {
        int databaseSizeBeforeCreate = reporteRepository.findAll().collectList().block().size();
        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeCreate + 1);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testReporte.getFechaGeneracion()).isEqualTo(DEFAULT_FECHA_GENERACION);
        assertThat(testReporte.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    void createReporteWithExistingId() throws Exception {
        // Create the Reporte with an existing ID
        reporte.setId("existing_id");
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        int databaseSizeBeforeCreate = reporteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = reporteRepository.findAll().collectList().block().size();
        // set the field null
        reporte.setTipo(null);

        // Create the Reporte, which fails.
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaGeneracionIsRequired() throws Exception {
        int databaseSizeBeforeTest = reporteRepository.findAll().collectList().block().size();
        // set the field null
        reporte.setFechaGeneracion(null);

        // Create the Reporte, which fails.
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllReportes() {
        // Initialize the database
        reporteRepository.save(reporte).block();

        // Get all the reporteList
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
            .value(hasItem(reporte.getId()))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO))
            .jsonPath("$.[*].fechaGeneracion")
            .value(hasItem(DEFAULT_FECHA_GENERACION.toString()))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsEnabled() {
        when(reporteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(reporteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsNotEnabled() {
        when(reporteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(reporteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getReporte() {
        // Initialize the database
        reporteRepository.save(reporte).block();

        // Get the reporte
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reporte.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(reporte.getId()))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO))
            .jsonPath("$.fechaGeneracion")
            .value(is(DEFAULT_FECHA_GENERACION.toString()))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION));
    }

    @Test
    void getNonExistingReporte() {
        // Get the reporte
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewReporte() throws Exception {
        // Initialize the database
        reporteRepository.save(reporte).block();

        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();

        // Update the reporte
        Reporte updatedReporte = reporteRepository.findById(reporte.getId()).block();
        updatedReporte.tipo(UPDATED_TIPO).fechaGeneracion(UPDATED_FECHA_GENERACION).descripcion(UPDATED_DESCRIPCION);
        ReporteDTO reporteDTO = reporteMapper.toDto(updatedReporte);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reporteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testReporte.getFechaGeneracion()).isEqualTo(UPDATED_FECHA_GENERACION);
        assertThat(testReporte.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void putNonExistingReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reporteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReporteWithPatch() throws Exception {
        // Initialize the database
        reporteRepository.save(reporte).block();

        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();

        // Update the reporte using partial update
        Reporte partialUpdatedReporte = new Reporte();
        partialUpdatedReporte.setId(reporte.getId());

        partialUpdatedReporte.fechaGeneracion(UPDATED_FECHA_GENERACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReporte.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReporte))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testReporte.getFechaGeneracion()).isEqualTo(UPDATED_FECHA_GENERACION);
        assertThat(testReporte.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    void fullUpdateReporteWithPatch() throws Exception {
        // Initialize the database
        reporteRepository.save(reporte).block();

        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();

        // Update the reporte using partial update
        Reporte partialUpdatedReporte = new Reporte();
        partialUpdatedReporte.setId(reporte.getId());

        partialUpdatedReporte.tipo(UPDATED_TIPO).fechaGeneracion(UPDATED_FECHA_GENERACION).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReporte.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReporte))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
        Reporte testReporte = reporteList.get(reporteList.size() - 1);
        assertThat(testReporte.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testReporte.getFechaGeneracion()).isEqualTo(UPDATED_FECHA_GENERACION);
        assertThat(testReporte.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void patchNonExistingReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reporteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReporte() throws Exception {
        int databaseSizeBeforeUpdate = reporteRepository.findAll().collectList().block().size();
        reporte.setId(UUID.randomUUID().toString());

        // Create the Reporte
        ReporteDTO reporteDTO = reporteMapper.toDto(reporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reporteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reporte in the database
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReporte() {
        // Initialize the database
        reporteRepository.save(reporte).block();

        int databaseSizeBeforeDelete = reporteRepository.findAll().collectList().block().size();

        // Delete the reporte
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reporte.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Reporte> reporteList = reporteRepository.findAll().collectList().block();
        assertThat(reporteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
