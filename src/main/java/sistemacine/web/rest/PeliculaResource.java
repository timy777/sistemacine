package sistemacine.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.repository.PeliculaRepository;
import sistemacine.service.PeliculaService;
import sistemacine.service.dto.PeliculaDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.Pelicula}.
 */
@RestController
@RequestMapping("/api")
public class PeliculaResource {

    private final Logger log = LoggerFactory.getLogger(PeliculaResource.class);

    private static final String ENTITY_NAME = "pelicula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeliculaService peliculaService;

    private final PeliculaRepository peliculaRepository;

    public PeliculaResource(PeliculaService peliculaService, PeliculaRepository peliculaRepository) {
        this.peliculaService = peliculaService;
        this.peliculaRepository = peliculaRepository;
    }

    /**
     * {@code POST  /peliculas} : Create a new pelicula.
     *
     * @param peliculaDTO the peliculaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peliculaDTO, or with status {@code 400 (Bad Request)} if the pelicula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/peliculas")
    public Mono<ResponseEntity<PeliculaDTO>> createPelicula(@Valid @RequestBody PeliculaDTO peliculaDTO) throws URISyntaxException {
        log.debug("REST request to save Pelicula : {}", peliculaDTO);
        if (peliculaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pelicula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return peliculaService
            .save(peliculaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/peliculas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /peliculas/:id} : Updates an existing pelicula.
     *
     * @param id the id of the peliculaDTO to save.
     * @param peliculaDTO the peliculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peliculaDTO,
     * or with status {@code 400 (Bad Request)} if the peliculaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peliculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/peliculas/{id}")
    public Mono<ResponseEntity<PeliculaDTO>> updatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeliculaDTO peliculaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pelicula : {}, {}", id, peliculaDTO);
        if (peliculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peliculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return peliculaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return peliculaService
                    .update(peliculaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /peliculas/:id} : Partial updates given fields of an existing pelicula, field will ignore if it is null
     *
     * @param id the id of the peliculaDTO to save.
     * @param peliculaDTO the peliculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peliculaDTO,
     * or with status {@code 400 (Bad Request)} if the peliculaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the peliculaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the peliculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/peliculas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PeliculaDTO>> partialUpdatePelicula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeliculaDTO peliculaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pelicula partially : {}, {}", id, peliculaDTO);
        if (peliculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peliculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return peliculaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PeliculaDTO> result = peliculaService.partialUpdate(peliculaDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /peliculas} : get all the peliculas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peliculas in body.
     */
    @GetMapping("/peliculas")
    public Mono<ResponseEntity<List<PeliculaDTO>>> getAllPeliculas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Peliculas");
        return peliculaService
            .countAll()
            .zipWith(peliculaService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /peliculas/:id} : get the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peliculaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/peliculas/{id}")
    public Mono<ResponseEntity<PeliculaDTO>> getPelicula(@PathVariable Long id) {
        log.debug("REST request to get Pelicula : {}", id);
        Mono<PeliculaDTO> peliculaDTO = peliculaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peliculaDTO);
    }

    /**
     * {@code DELETE  /peliculas/:id} : delete the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/peliculas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePelicula(@PathVariable Long id) {
        log.debug("REST request to delete Pelicula : {}", id);
        return peliculaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
