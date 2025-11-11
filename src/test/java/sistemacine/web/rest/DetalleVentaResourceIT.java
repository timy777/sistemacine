package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static sistemacine.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
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
import sistemacine.domain.DetalleVenta;
import sistemacine.repository.DetalleVentaRepository;
import sistemacine.service.dto.DetalleVentaDTO;
import sistemacine.service.mapper.DetalleVentaMapper;

/**
 * Integration tests for the {@link DetalleVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DetalleVentaResourceIT {

    private static final String DEFAULT_ASIENTO = "AAAAAAAAAA";
    private static final String UPDATED_ASIENTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_UNITARIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_UNITARIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/detalle-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private DetalleVentaMapper detalleVentaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private DetalleVenta detalleVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createEntity() {
        DetalleVenta detalleVenta = new DetalleVenta().asiento(DEFAULT_ASIENTO).precioUnitario(DEFAULT_PRECIO_UNITARIO);
        return detalleVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createUpdatedEntity() {
        DetalleVenta detalleVenta = new DetalleVenta().asiento(UPDATED_ASIENTO).precioUnitario(UPDATED_PRECIO_UNITARIO);
        return detalleVenta;
    }

    @BeforeEach
    public void initTest() {
        detalleVentaRepository.deleteAll().block();
        detalleVenta = createEntity();
    }

    @Test
    void createDetalleVenta() throws Exception {
        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().collectList().block().size();
        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getAsiento()).isEqualTo(DEFAULT_ASIENTO);
        assertThat(testDetalleVenta.getPrecioUnitario()).isEqualByComparingTo(DEFAULT_PRECIO_UNITARIO);
    }

    @Test
    void createDetalleVentaWithExistingId() throws Exception {
        // Create the DetalleVenta with an existing ID
        detalleVenta.setId("existing_id");
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAsientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = detalleVentaRepository.findAll().collectList().block().size();
        // set the field null
        detalleVenta.setAsiento(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrecioUnitarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = detalleVentaRepository.findAll().collectList().block().size();
        // set the field null
        detalleVenta.setPrecioUnitario(null);

        // Create the DetalleVenta, which fails.
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDetalleVentas() {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        // Get all the detalleVentaList
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
            .value(hasItem(detalleVenta.getId()))
            .jsonPath("$.[*].asiento")
            .value(hasItem(DEFAULT_ASIENTO))
            .jsonPath("$.[*].precioUnitario")
            .value(hasItem(sameNumber(DEFAULT_PRECIO_UNITARIO)));
    }

    @Test
    void getDetalleVenta() {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        // Get the detalleVenta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, detalleVenta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(detalleVenta.getId()))
            .jsonPath("$.asiento")
            .value(is(DEFAULT_ASIENTO))
            .jsonPath("$.precioUnitario")
            .value(is(sameNumber(DEFAULT_PRECIO_UNITARIO)));
    }

    @Test
    void getNonExistingDetalleVenta() {
        // Get the detalleVenta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();

        // Update the detalleVenta
        DetalleVenta updatedDetalleVenta = detalleVentaRepository.findById(detalleVenta.getId()).block();
        updatedDetalleVenta.asiento(UPDATED_ASIENTO).precioUnitario(UPDATED_PRECIO_UNITARIO);
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(updatedDetalleVenta);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detalleVentaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getAsiento()).isEqualTo(UPDATED_ASIENTO);
        assertThat(testDetalleVenta.getPrecioUnitario()).isEqualByComparingTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    void putNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, detalleVentaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getAsiento()).isEqualTo(DEFAULT_ASIENTO);
        assertThat(testDetalleVenta.getPrecioUnitario()).isEqualByComparingTo(DEFAULT_PRECIO_UNITARIO);
    }

    @Test
    void fullUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta.asiento(UPDATED_ASIENTO).precioUnitario(UPDATED_PRECIO_UNITARIO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getAsiento()).isEqualTo(UPDATED_ASIENTO);
        assertThat(testDetalleVenta.getPrecioUnitario()).isEqualByComparingTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    void patchNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, detalleVentaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().collectList().block().size();
        detalleVenta.setId(UUID.randomUUID().toString());

        // Create the DetalleVenta
        DetalleVentaDTO detalleVentaDTO = detalleVentaMapper.toDto(detalleVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(detalleVentaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDetalleVenta() {
        // Initialize the database
        detalleVentaRepository.save(detalleVenta).block();

        int databaseSizeBeforeDelete = detalleVentaRepository.findAll().collectList().block().size();

        // Delete the detalleVenta
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, detalleVenta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll().collectList().block();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
