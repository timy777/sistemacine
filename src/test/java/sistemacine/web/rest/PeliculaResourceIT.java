package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.IntegrationTest;
import sistemacine.domain.Pelicula;
import sistemacine.domain.enumeration.EstadoPelicula;
import sistemacine.repository.EntityManager;
import sistemacine.repository.PeliculaRepository;
import sistemacine.service.PeliculaService;
import sistemacine.service.dto.PeliculaDTO;
import sistemacine.service.mapper.PeliculaMapper;

/**
 * Integration tests for the {@link PeliculaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PeliculaResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_SINOPSIS = "AAAAAAAAAA";
    private static final String UPDATED_SINOPSIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURACION = 1;
    private static final Integer UPDATED_DURACION = 2;

    private static final String DEFAULT_IDIOMA = "AAAAAAAAAA";
    private static final String UPDATED_IDIOMA = "BBBBBBBBBB";

    private static final String DEFAULT_CLASIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_CLASIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_FORMATO = "AAAAAAAAAA";
    private static final String UPDATED_FORMATO = "BBBBBBBBBB";

    private static final EstadoPelicula DEFAULT_ESTADO = EstadoPelicula.EN_CARTELERA;
    private static final EstadoPelicula UPDATED_ESTADO = EstadoPelicula.PROXIMO_ESTRENO;

    private static final String DEFAULT_IMAGEN_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/peliculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Mock
    private PeliculaRepository peliculaRepositoryMock;

    @Autowired
    private PeliculaMapper peliculaMapper;

    @Mock
    private PeliculaService peliculaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Pelicula pelicula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .titulo(DEFAULT_TITULO)
            .sinopsis(DEFAULT_SINOPSIS)
            .duracion(DEFAULT_DURACION)
            .idioma(DEFAULT_IDIOMA)
            .clasificacion(DEFAULT_CLASIFICACION)
            .formato(DEFAULT_FORMATO)
            .estado(DEFAULT_ESTADO)
            .imagenUrl(DEFAULT_IMAGEN_URL);
        return pelicula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createUpdatedEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .duracion(UPDATED_DURACION)
            .idioma(UPDATED_IDIOMA)
            .clasificacion(UPDATED_CLASIFICACION)
            .formato(UPDATED_FORMATO)
            .estado(UPDATED_ESTADO)
            .imagenUrl(UPDATED_IMAGEN_URL);
        return pelicula;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Pelicula.class).block();
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
        pelicula = createEntity(em);
    }

    @Test
    void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().collectList().block().size();
        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(DEFAULT_SINOPSIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(DEFAULT_DURACION);
        assertThat(testPelicula.getIdioma()).isEqualTo(DEFAULT_IDIOMA);
        assertThat(testPelicula.getClasificacion()).isEqualTo(DEFAULT_CLASIFICACION);
        assertThat(testPelicula.getFormato()).isEqualTo(DEFAULT_FORMATO);
        assertThat(testPelicula.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPelicula.getImagenUrl()).isEqualTo(DEFAULT_IMAGEN_URL);
    }

    @Test
    void createPeliculaWithExistingId() throws Exception {
        // Create the Pelicula with an existing ID
        pelicula.setId(1L);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        int databaseSizeBeforeCreate = peliculaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().collectList().block().size();
        // set the field null
        pelicula.setTitulo(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDuracionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().collectList().block().size();
        // set the field null
        pelicula.setDuracion(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().collectList().block().size();
        // set the field null
        pelicula.setEstado(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPeliculas() {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        // Get all the peliculaList
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
            .value(hasItem(pelicula.getId().intValue()))
            .jsonPath("$.[*].titulo")
            .value(hasItem(DEFAULT_TITULO))
            .jsonPath("$.[*].sinopsis")
            .value(hasItem(DEFAULT_SINOPSIS.toString()))
            .jsonPath("$.[*].duracion")
            .value(hasItem(DEFAULT_DURACION))
            .jsonPath("$.[*].idioma")
            .value(hasItem(DEFAULT_IDIOMA))
            .jsonPath("$.[*].clasificacion")
            .value(hasItem(DEFAULT_CLASIFICACION))
            .jsonPath("$.[*].formato")
            .value(hasItem(DEFAULT_FORMATO))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.toString()))
            .jsonPath("$.[*].imagenUrl")
            .value(hasItem(DEFAULT_IMAGEN_URL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeliculasWithEagerRelationshipsIsEnabled() {
        when(peliculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(peliculaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeliculasWithEagerRelationshipsIsNotEnabled() {
        when(peliculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(peliculaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPelicula() {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        // Get the pelicula
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pelicula.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pelicula.getId().intValue()))
            .jsonPath("$.titulo")
            .value(is(DEFAULT_TITULO))
            .jsonPath("$.sinopsis")
            .value(is(DEFAULT_SINOPSIS.toString()))
            .jsonPath("$.duracion")
            .value(is(DEFAULT_DURACION))
            .jsonPath("$.idioma")
            .value(is(DEFAULT_IDIOMA))
            .jsonPath("$.clasificacion")
            .value(is(DEFAULT_CLASIFICACION))
            .jsonPath("$.formato")
            .value(is(DEFAULT_FORMATO))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.toString()))
            .jsonPath("$.imagenUrl")
            .value(is(DEFAULT_IMAGEN_URL));
    }

    @Test
    void getNonExistingPelicula() {
        // Get the pelicula
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findById(pelicula.getId()).block();
        updatedPelicula
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .duracion(UPDATED_DURACION)
            .idioma(UPDATED_IDIOMA)
            .clasificacion(UPDATED_CLASIFICACION)
            .formato(UPDATED_FORMATO)
            .estado(UPDATED_ESTADO)
            .imagenUrl(UPDATED_IMAGEN_URL);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(updatedPelicula);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, peliculaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(UPDATED_SINOPSIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(UPDATED_DURACION);
        assertThat(testPelicula.getIdioma()).isEqualTo(UPDATED_IDIOMA);
        assertThat(testPelicula.getClasificacion()).isEqualTo(UPDATED_CLASIFICACION);
        assertThat(testPelicula.getFormato()).isEqualTo(UPDATED_FORMATO);
        assertThat(testPelicula.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPelicula.getImagenUrl()).isEqualTo(UPDATED_IMAGEN_URL);
    }

    @Test
    void putNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, peliculaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        partialUpdatedPelicula.idioma(UPDATED_IDIOMA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(DEFAULT_SINOPSIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(DEFAULT_DURACION);
        assertThat(testPelicula.getIdioma()).isEqualTo(UPDATED_IDIOMA);
        assertThat(testPelicula.getClasificacion()).isEqualTo(DEFAULT_CLASIFICACION);
        assertThat(testPelicula.getFormato()).isEqualTo(DEFAULT_FORMATO);
        assertThat(testPelicula.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPelicula.getImagenUrl()).isEqualTo(DEFAULT_IMAGEN_URL);
    }

    @Test
    void fullUpdatePeliculaWithPatch() throws Exception {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();

        // Update the pelicula using partial update
        Pelicula partialUpdatedPelicula = new Pelicula();
        partialUpdatedPelicula.setId(pelicula.getId());

        partialUpdatedPelicula
            .titulo(UPDATED_TITULO)
            .sinopsis(UPDATED_SINOPSIS)
            .duracion(UPDATED_DURACION)
            .idioma(UPDATED_IDIOMA)
            .clasificacion(UPDATED_CLASIFICACION)
            .formato(UPDATED_FORMATO)
            .estado(UPDATED_ESTADO)
            .imagenUrl(UPDATED_IMAGEN_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPelicula.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPelicula))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(UPDATED_SINOPSIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(UPDATED_DURACION);
        assertThat(testPelicula.getIdioma()).isEqualTo(UPDATED_IDIOMA);
        assertThat(testPelicula.getClasificacion()).isEqualTo(UPDATED_CLASIFICACION);
        assertThat(testPelicula.getFormato()).isEqualTo(UPDATED_FORMATO);
        assertThat(testPelicula.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPelicula.getImagenUrl()).isEqualTo(UPDATED_IMAGEN_URL);
    }

    @Test
    void patchNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, peliculaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().collectList().block().size();
        pelicula.setId(count.incrementAndGet());

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(peliculaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePelicula() {
        // Initialize the database
        peliculaRepository.save(pelicula).block();

        int databaseSizeBeforeDelete = peliculaRepository.findAll().collectList().block().size();

        // Delete the pelicula
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pelicula.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Pelicula> peliculaList = peliculaRepository.findAll().collectList().block();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
