package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

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
import sistemacine.domain.Genero;
import sistemacine.repository.EntityManager;
import sistemacine.repository.GeneroRepository;
import sistemacine.service.dto.GeneroDTO;
import sistemacine.service.mapper.GeneroMapper;

/**
 * Integration tests for the {@link GeneroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GeneroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/generos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private GeneroMapper generoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Genero genero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createEntity(EntityManager em) {
        Genero genero = new Genero().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return genero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createUpdatedEntity(EntityManager em) {
        Genero genero = new Genero().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return genero;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Genero.class).block();
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
        genero = createEntity(em);
    }

    @Test
    void createGenero() throws Exception {
        int databaseSizeBeforeCreate = generoRepository.findAll().collectList().block().size();
        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate + 1);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testGenero.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    void createGeneroWithExistingId() throws Exception {
        // Create the Genero with an existing ID
        genero.setId(1L);
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        int databaseSizeBeforeCreate = generoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = generoRepository.findAll().collectList().block().size();
        // set the field null
        genero.setNombre(null);

        // Create the Genero, which fails.
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGeneros() {
        // Initialize the database
        generoRepository.save(genero).block();

        // Get all the generoList
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
            .value(hasItem(genero.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION));
    }

    @Test
    void getGenero() {
        // Initialize the database
        generoRepository.save(genero).block();

        // Get the genero
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, genero.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(genero.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION));
    }

    @Test
    void getNonExistingGenero() {
        // Get the genero
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGenero() throws Exception {
        // Initialize the database
        generoRepository.save(genero).block();

        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();

        // Update the genero
        Genero updatedGenero = generoRepository.findById(genero.getId()).block();
        updatedGenero.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        GeneroDTO generoDTO = generoMapper.toDto(updatedGenero);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, generoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testGenero.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void putNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, generoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.save(genero).block();

        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        partialUpdatedGenero.descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testGenero.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void fullUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.save(genero).block();

        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        partialUpdatedGenero.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testGenero.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    void patchNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, generoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().collectList().block().size();
        genero.setId(count.incrementAndGet());

        // Create the Genero
        GeneroDTO generoDTO = generoMapper.toDto(genero);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(generoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGenero() {
        // Initialize the database
        generoRepository.save(genero).block();

        int databaseSizeBeforeDelete = generoRepository.findAll().collectList().block().size();

        // Delete the genero
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, genero.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Genero> generoList = generoRepository.findAll().collectList().block();
        assertThat(generoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
