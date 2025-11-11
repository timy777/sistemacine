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
import sistemacine.repository.GeneroRepository;
import sistemacine.service.GeneroService;
import sistemacine.service.dto.GeneroDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.Genero}.
 */
@RestController
@RequestMapping("/api")
public class GeneroResource {

    private final Logger log = LoggerFactory.getLogger(GeneroResource.class);

    private static final String ENTITY_NAME = "genero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneroService generoService;

    private final GeneroRepository generoRepository;

    public GeneroResource(GeneroService generoService, GeneroRepository generoRepository) {
        this.generoService = generoService;
        this.generoRepository = generoRepository;
    }

    /**
     * {@code POST  /generos} : Create a new genero.
     *
     * @param generoDTO the generoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new generoDTO, or with status {@code 400 (Bad Request)} if the genero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generos")
    public Mono<ResponseEntity<GeneroDTO>> createGenero(@Valid @RequestBody GeneroDTO generoDTO) throws URISyntaxException {
        log.debug("REST request to save Genero : {}", generoDTO);
        if (generoDTO.getId() != null) {
            throw new BadRequestAlertException("A new genero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return generoService
            .save(generoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/generos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /generos/:id} : Updates an existing genero.
     *
     * @param id the id of the generoDTO to save.
     * @param generoDTO the generoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generoDTO,
     * or with status {@code 400 (Bad Request)} if the generoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the generoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generos/{id}")
    public Mono<ResponseEntity<GeneroDTO>> updateGenero(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody GeneroDTO generoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Genero : {}, {}", id, generoDTO);
        if (generoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return generoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return generoService
                    .update(generoDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /generos/:id} : Partial updates given fields of an existing genero, field will ignore if it is null
     *
     * @param id the id of the generoDTO to save.
     * @param generoDTO the generoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated generoDTO,
     * or with status {@code 400 (Bad Request)} if the generoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the generoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the generoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GeneroDTO>> partialUpdateGenero(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody GeneroDTO generoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Genero partially : {}, {}", id, generoDTO);
        if (generoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return generoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GeneroDTO> result = generoService.partialUpdate(generoDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /generos} : get all the generos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generos in body.
     */
    @GetMapping("/generos")
    public Mono<ResponseEntity<List<GeneroDTO>>> getAllGeneros(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Generos");
        return generoService
            .countAll()
            .zipWith(generoService.findAll(pageable).collectList())
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
     * {@code GET  /generos/:id} : get the "id" genero.
     *
     * @param id the id of the generoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the generoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generos/{id}")
    public Mono<ResponseEntity<GeneroDTO>> getGenero(@PathVariable String id) {
        log.debug("REST request to get Genero : {}", id);
        Mono<GeneroDTO> generoDTO = generoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generoDTO);
    }

    /**
     * {@code DELETE  /generos/:id} : delete the "id" genero.
     *
     * @param id the id of the generoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGenero(@PathVariable String id) {
        log.debug("REST request to delete Genero : {}", id);
        return generoService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
