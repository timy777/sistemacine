package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static sistemacine.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
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
import sistemacine.domain.Venta;
import sistemacine.repository.EntityManager;
import sistemacine.repository.VentaRepository;
import sistemacine.service.VentaService;
import sistemacine.service.dto.VentaDTO;
import sistemacine.service.mapper.VentaMapper;

/**
 * Integration tests for the {@link VentaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VentaResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    private static final String DEFAULT_METODO_PAGO = "AAAAAAAAAA";
    private static final String UPDATED_METODO_PAGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VentaRepository ventaRepository;

    @Mock
    private VentaRepository ventaRepositoryMock;

    @Autowired
    private VentaMapper ventaMapper;

    @Mock
    private VentaService ventaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Venta venta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity(EntityManager em) {
        Venta venta = new Venta().fecha(DEFAULT_FECHA).total(DEFAULT_TOTAL).metodoPago(DEFAULT_METODO_PAGO);
        return venta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createUpdatedEntity(EntityManager em) {
        Venta venta = new Venta().fecha(UPDATED_FECHA).total(UPDATED_TOTAL).metodoPago(UPDATED_METODO_PAGO);
        return venta;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Venta.class).block();
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
        venta = createEntity(em);
    }

    @Test
    void createVenta() throws Exception {
        int databaseSizeBeforeCreate = ventaRepository.findAll().collectList().block().size();
        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate + 1);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testVenta.getTotal()).isEqualByComparingTo(DEFAULT_TOTAL);
        assertThat(testVenta.getMetodoPago()).isEqualTo(DEFAULT_METODO_PAGO);
    }

    @Test
    void createVentaWithExistingId() throws Exception {
        // Create the Venta with an existing ID
        venta.setId(1L);
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        int databaseSizeBeforeCreate = ventaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().collectList().block().size();
        // set the field null
        venta.setFecha(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().collectList().block().size();
        // set the field null
        venta.setTotal(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMetodoPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().collectList().block().size();
        // set the field null
        venta.setMetodoPago(null);

        // Create the Venta, which fails.
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllVentas() {
        // Initialize the database
        ventaRepository.save(venta).block();

        // Get all the ventaList
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
            .value(hasItem(venta.getId().intValue()))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].total")
            .value(hasItem(sameNumber(DEFAULT_TOTAL)))
            .jsonPath("$.[*].metodoPago")
            .value(hasItem(DEFAULT_METODO_PAGO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentasWithEagerRelationshipsIsEnabled() {
        when(ventaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(ventaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVentasWithEagerRelationshipsIsNotEnabled() {
        when(ventaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(ventaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getVenta() {
        // Initialize the database
        ventaRepository.save(venta).block();

        // Get the venta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, venta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(venta.getId().intValue()))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.total")
            .value(is(sameNumber(DEFAULT_TOTAL)))
            .jsonPath("$.metodoPago")
            .value(is(DEFAULT_METODO_PAGO));
    }

    @Test
    void getNonExistingVenta() {
        // Get the venta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewVenta() throws Exception {
        // Initialize the database
        ventaRepository.save(venta).block();

        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).block();
        updatedVenta.fecha(UPDATED_FECHA).total(UPDATED_TOTAL).metodoPago(UPDATED_METODO_PAGO);
        VentaDTO ventaDTO = ventaMapper.toDto(updatedVenta);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ventaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testVenta.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
    }

    @Test
    void putNonExistingVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ventaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        ventaRepository.save(venta).block();

        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.total(UPDATED_TOTAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVenta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testVenta.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testVenta.getMetodoPago()).isEqualTo(DEFAULT_METODO_PAGO);
    }

    @Test
    void fullUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        ventaRepository.save(venta).block();

        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.fecha(UPDATED_FECHA).total(UPDATED_TOTAL).metodoPago(UPDATED_METODO_PAGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVenta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getTotal()).isEqualByComparingTo(UPDATED_TOTAL);
        assertThat(testVenta.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
    }

    @Test
    void patchNonExistingVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ventaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().collectList().block().size();
        venta.setId(count.incrementAndGet());

        // Create the Venta
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ventaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVenta() {
        // Initialize the database
        ventaRepository.save(venta).block();

        int databaseSizeBeforeDelete = ventaRepository.findAll().collectList().block().size();

        // Delete the venta
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, venta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Venta> ventaList = ventaRepository.findAll().collectList().block();
        assertThat(ventaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
