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
import sistemacine.repository.FuncionRepository;
import sistemacine.service.FuncionService;
import sistemacine.service.dto.FuncionDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.Funcion}.
 */
@RestController
@RequestMapping("/api")
public class FuncionResource {

    private final Logger log = LoggerFactory.getLogger(FuncionResource.class);

    private static final String ENTITY_NAME = "funcion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncionService funcionService;

    private final FuncionRepository funcionRepository;

    public FuncionResource(FuncionService funcionService, FuncionRepository funcionRepository) {
        this.funcionService = funcionService;
        this.funcionRepository = funcionRepository;
    }

    /**
     * {@code POST  /funcions} : Create a new funcion.
     *
     * @param funcionDTO the funcionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionDTO, or with status {@code 400 (Bad Request)} if the funcion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funcions")
    public Mono<ResponseEntity<FuncionDTO>> createFuncion(@Valid @RequestBody FuncionDTO funcionDTO) throws URISyntaxException {
        log.debug("REST request to save Funcion : {}", funcionDTO);
        if (funcionDTO.getId() != null) {
            throw new BadRequestAlertException("A new funcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return funcionService
            .save(funcionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/funcions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /funcions/:id} : Updates an existing funcion.
     *
     * @param id the id of the funcionDTO to save.
     * @param funcionDTO the funcionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionDTO,
     * or with status {@code 400 (Bad Request)} if the funcionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funcions/{id}")
    public Mono<ResponseEntity<FuncionDTO>> updateFuncion(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FuncionDTO funcionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Funcion : {}, {}", id, funcionDTO);
        if (funcionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return funcionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return funcionService
                    .update(funcionDTO)
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
     * {@code PATCH  /funcions/:id} : Partial updates given fields of an existing funcion, field will ignore if it is null
     *
     * @param id the id of the funcionDTO to save.
     * @param funcionDTO the funcionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionDTO,
     * or with status {@code 400 (Bad Request)} if the funcionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the funcionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funcions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FuncionDTO>> partialUpdateFuncion(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FuncionDTO funcionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funcion partially : {}, {}", id, funcionDTO);
        if (funcionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return funcionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FuncionDTO> result = funcionService.partialUpdate(funcionDTO);

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
     * {@code GET  /funcions} : get all the funcions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcions in body.
     */
    @GetMapping("/funcions")
    public Mono<ResponseEntity<List<FuncionDTO>>> getAllFuncions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Funcions");
        return funcionService
            .countAll()
            .zipWith(funcionService.findAll(pageable).collectList())
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
     * {@code GET  /funcions/:id} : get the "id" funcion.
     *
     * @param id the id of the funcionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funcions/{id}")
    public Mono<ResponseEntity<FuncionDTO>> getFuncion(@PathVariable String id) {
        log.debug("REST request to get Funcion : {}", id);
        Mono<FuncionDTO> funcionDTO = funcionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funcionDTO);
    }

    /**
     * {@code DELETE  /funcions/:id} : delete the "id" funcion.
     *
     * @param id the id of the funcionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funcions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFuncion(@PathVariable String id) {
        log.debug("REST request to delete Funcion : {}", id);
        return funcionService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
