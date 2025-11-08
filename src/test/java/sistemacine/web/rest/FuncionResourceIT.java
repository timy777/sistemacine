package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static sistemacine.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.IntegrationTest;
import sistemacine.domain.Funcion;
import sistemacine.repository.EntityManager;
import sistemacine.repository.FuncionRepository;
import sistemacine.service.FuncionService;
import sistemacine.service.dto.FuncionDTO;
import sistemacine.service.mapper.FuncionMapper;

/**
 * Integration tests for the {@link FuncionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FuncionResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_HORA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HORA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/funcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncionRepository funcionRepository;

    @Mock
    private FuncionRepository funcionRepositoryMock;

    @Autowired
    private FuncionMapper funcionMapper;

    @Mock
    private FuncionService funcionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Funcion funcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcion createEntity(EntityManager em) {
        Funcion funcion = new Funcion()
            .fecha(DEFAULT_FECHA)
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFin(DEFAULT_HORA_FIN)
            .precio(DEFAULT_PRECIO);
        return funcion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funcion createUpdatedEntity(EntityManager em) {
        Funcion funcion = new Funcion()
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN)
            .precio(UPDATED_PRECIO);
        return funcion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Funcion.class).block();
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
        funcion = createEntity(em);
    }

    @Test
    void createFuncion() throws Exception {
        int databaseSizeBeforeCreate = funcionRepository.findAll().collectList().block().size();
        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeCreate + 1);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFuncion.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testFuncion.getHoraFin()).isEqualTo(DEFAULT_HORA_FIN);
        assertThat(testFuncion.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
    }

    @Test
    void createFuncionWithExistingId() throws Exception {
        // Create the Funcion with an existing ID
        funcion.setId(1L);
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        int databaseSizeBeforeCreate = funcionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().collectList().block().size();
        // set the field null
        funcion.setFecha(null);

        // Create the Funcion, which fails.
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().collectList().block().size();
        // set the field null
        funcion.setHoraInicio(null);

        // Create the Funcion, which fails.
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkHoraFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().collectList().block().size();
        // set the field null
        funcion.setHoraFin(null);

        // Create the Funcion, which fails.
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionRepository.findAll().collectList().block().size();
        // set the field null
        funcion.setPrecio(null);

        // Create the Funcion, which fails.
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFuncions() {
        // Initialize the database
        funcionRepository.save(funcion).block();

        // Get all the funcionList
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
            .value(hasItem(funcion.getId().intValue()))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].horaInicio")
            .value(hasItem(DEFAULT_HORA_INICIO.toString()))
            .jsonPath("$.[*].horaFin")
            .value(hasItem(DEFAULT_HORA_FIN.toString()))
            .jsonPath("$.[*].precio")
            .value(hasItem(sameNumber(DEFAULT_PRECIO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionsWithEagerRelationshipsIsEnabled() {
        when(funcionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(funcionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFuncionsWithEagerRelationshipsIsNotEnabled() {
        when(funcionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(funcionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFuncion() {
        // Initialize the database
        funcionRepository.save(funcion).block();

        // Get the funcion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, funcion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(funcion.getId().intValue()))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.horaInicio")
            .value(is(DEFAULT_HORA_INICIO.toString()))
            .jsonPath("$.horaFin")
            .value(is(DEFAULT_HORA_FIN.toString()))
            .jsonPath("$.precio")
            .value(is(sameNumber(DEFAULT_PRECIO)));
    }

    @Test
    void getNonExistingFuncion() {
        // Get the funcion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFuncion() throws Exception {
        // Initialize the database
        funcionRepository.save(funcion).block();

        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();

        // Update the funcion
        Funcion updatedFuncion = funcionRepository.findById(funcion.getId()).block();
        updatedFuncion.fecha(UPDATED_FECHA).horaInicio(UPDATED_HORA_INICIO).horaFin(UPDATED_HORA_FIN).precio(UPDATED_PRECIO);
        FuncionDTO funcionDTO = funcionMapper.toDto(updatedFuncion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, funcionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFuncion.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testFuncion.getHoraFin()).isEqualTo(UPDATED_HORA_FIN);
        assertThat(testFuncion.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    void putNonExistingFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, funcionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFuncionWithPatch() throws Exception {
        // Initialize the database
        funcionRepository.save(funcion).block();

        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();

        // Update the funcion using partial update
        Funcion partialUpdatedFuncion = new Funcion();
        partialUpdatedFuncion.setId(funcion.getId());

        partialUpdatedFuncion.horaInicio(UPDATED_HORA_INICIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFuncion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFuncion.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testFuncion.getHoraFin()).isEqualTo(DEFAULT_HORA_FIN);
        assertThat(testFuncion.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
    }

    @Test
    void fullUpdateFuncionWithPatch() throws Exception {
        // Initialize the database
        funcionRepository.save(funcion).block();

        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();

        // Update the funcion using partial update
        Funcion partialUpdatedFuncion = new Funcion();
        partialUpdatedFuncion.setId(funcion.getId());

        partialUpdatedFuncion.fecha(UPDATED_FECHA).horaInicio(UPDATED_HORA_INICIO).horaFin(UPDATED_HORA_FIN).precio(UPDATED_PRECIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFuncion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
        Funcion testFuncion = funcionList.get(funcionList.size() - 1);
        assertThat(testFuncion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFuncion.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testFuncion.getHoraFin()).isEqualTo(UPDATED_HORA_FIN);
        assertThat(testFuncion.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    void patchNonExistingFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, funcionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFuncion() throws Exception {
        int databaseSizeBeforeUpdate = funcionRepository.findAll().collectList().block().size();
        funcion.setId(count.incrementAndGet());

        // Create the Funcion
        FuncionDTO funcionDTO = funcionMapper.toDto(funcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(funcionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Funcion in the database
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFuncion() {
        // Initialize the database
        funcionRepository.save(funcion).block();

        int databaseSizeBeforeDelete = funcionRepository.findAll().collectList().block().size();

        // Delete the funcion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, funcion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Funcion> funcionList = funcionRepository.findAll().collectList().block();
        assertThat(funcionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
