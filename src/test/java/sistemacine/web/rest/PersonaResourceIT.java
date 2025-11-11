package sistemacine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sistemacine.domain.Persona;
import sistemacine.domain.enumeration.Sexo;
import sistemacine.domain.enumeration.TipoPersona;
import sistemacine.repository.PersonaRepository;
import sistemacine.service.dto.PersonaDTO;
import sistemacine.service.mapper.PersonaMapper;

/**
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PersonaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final TipoPersona DEFAULT_TIPO = TipoPersona.CLIENTE;
    private static final TipoPersona UPDATED_TIPO = TipoPersona.VENDEDOR;

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Sexo DEFAULT_SEXO = Sexo.MASCULINO;
    private static final Sexo UPDATED_SEXO = Sexo.FEMENINO;

    private static final String DEFAULT_CARNET_IDENTIDAD = "AAAAAAAAAA";
    private static final String UPDATED_CARNET_IDENTIDAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaMapper personaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Persona persona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity() {
        Persona persona = new Persona()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .tipo(DEFAULT_TIPO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .sexo(DEFAULT_SEXO)
            .carnetIdentidad(DEFAULT_CARNET_IDENTIDAD);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity() {
        Persona persona = new Persona()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .tipo(UPDATED_TIPO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .carnetIdentidad(UPDATED_CARNET_IDENTIDAD);
        return persona;
    }

    @BeforeEach
    public void initTest() {
        personaRepository.deleteAll().block();
        persona = createEntity();
    }

    @Test
    void createPersona() throws Exception {
        int databaseSizeBeforeCreate = personaRepository.findAll().collectList().block().size();
        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate + 1);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersona.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testPersona.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPersona.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersona.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testPersona.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPersona.getCarnetIdentidad()).isEqualTo(DEFAULT_CARNET_IDENTIDAD);
    }

    @Test
    void createPersonaWithExistingId() throws Exception {
        // Create the Persona with an existing ID
        persona.setId("existing_id");
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        int databaseSizeBeforeCreate = personaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setNombre(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setApellido(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setEmail(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setTipo(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setFechaNacimiento(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSexoIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setSexo(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCarnetIdentidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = personaRepository.findAll().collectList().block().size();
        // set the field null
        persona.setCarnetIdentidad(null);

        // Create the Persona, which fails.
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPersonas() {
        // Initialize the database
        personaRepository.save(persona).block();

        // Get all the personaList
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
            .value(hasItem(persona.getId()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].apellido")
            .value(hasItem(DEFAULT_APELLIDO))
            .jsonPath("$.[*].telefono")
            .value(hasItem(DEFAULT_TELEFONO))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO.toString()))
            .jsonPath("$.[*].fechaNacimiento")
            .value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.[*].sexo")
            .value(hasItem(DEFAULT_SEXO.toString()))
            .jsonPath("$.[*].carnetIdentidad")
            .value(hasItem(DEFAULT_CARNET_IDENTIDAD));
    }

    @Test
    void getPersona() {
        // Initialize the database
        personaRepository.save(persona).block();

        // Get the persona
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(persona.getId()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.apellido")
            .value(is(DEFAULT_APELLIDO))
            .jsonPath("$.telefono")
            .value(is(DEFAULT_TELEFONO))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO.toString()))
            .jsonPath("$.fechaNacimiento")
            .value(is(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.sexo")
            .value(is(DEFAULT_SEXO.toString()))
            .jsonPath("$.carnetIdentidad")
            .value(is(DEFAULT_CARNET_IDENTIDAD));
    }

    @Test
    void getNonExistingPersona() {
        // Get the persona
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPersona() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).block();
        updatedPersona
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .tipo(UPDATED_TIPO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .carnetIdentidad(UPDATED_CARNET_IDENTIDAD);
        PersonaDTO personaDTO = personaMapper.toDto(updatedPersona);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, personaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPersona.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPersona.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersona.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPersona.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPersona.getCarnetIdentidad()).isEqualTo(UPDATED_CARNET_IDENTIDAD);
    }

    @Test
    void putNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, personaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .tipo(UPDATED_TIPO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .carnetIdentidad(UPDATED_CARNET_IDENTIDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPersona.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testPersona.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPersona.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersona.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPersona.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPersona.getCarnetIdentidad()).isEqualTo(UPDATED_CARNET_IDENTIDAD);
    }

    @Test
    void fullUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .tipo(UPDATED_TIPO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .carnetIdentidad(UPDATED_CARNET_IDENTIDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPersona))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
        Persona testPersona = personaList.get(personaList.size() - 1);
        assertThat(testPersona.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPersona.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testPersona.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPersona.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersona.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testPersona.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testPersona.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPersona.getCarnetIdentidad()).isEqualTo(UPDATED_CARNET_IDENTIDAD);
    }

    @Test
    void patchNonExistingPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, personaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPersona() throws Exception {
        int databaseSizeBeforeUpdate = personaRepository.findAll().collectList().block().size();
        persona.setId(UUID.randomUUID().toString());

        // Create the Persona
        PersonaDTO personaDTO = personaMapper.toDto(persona);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(personaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Persona in the database
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePersona() {
        // Initialize the database
        personaRepository.save(persona).block();

        int databaseSizeBeforeDelete = personaRepository.findAll().collectList().block().size();

        // Delete the persona
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, persona.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Persona> personaList = personaRepository.findAll().collectList().block();
        assertThat(personaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
