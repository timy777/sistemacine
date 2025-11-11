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
import sistemacine.repository.TarifaRepository;
import sistemacine.service.TarifaService;
import sistemacine.service.dto.TarifaDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.Tarifa}.
 */
@RestController
@RequestMapping("/api")
public class TarifaResource {

    private final Logger log = LoggerFactory.getLogger(TarifaResource.class);

    private static final String ENTITY_NAME = "tarifa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TarifaService tarifaService;

    private final TarifaRepository tarifaRepository;

    public TarifaResource(TarifaService tarifaService, TarifaRepository tarifaRepository) {
        this.tarifaService = tarifaService;
        this.tarifaRepository = tarifaRepository;
    }

    /**
     * {@code POST  /tarifas} : Create a new tarifa.
     *
     * @param tarifaDTO the tarifaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarifaDTO, or with status {@code 400 (Bad Request)} if the tarifa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tarifas")
    public Mono<ResponseEntity<TarifaDTO>> createTarifa(@Valid @RequestBody TarifaDTO tarifaDTO) throws URISyntaxException {
        log.debug("REST request to save Tarifa : {}", tarifaDTO);
        if (tarifaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarifa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tarifaService
            .save(tarifaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tarifas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tarifas/:id} : Updates an existing tarifa.
     *
     * @param id the id of the tarifaDTO to save.
     * @param tarifaDTO the tarifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tarifas/{id}")
    public Mono<ResponseEntity<TarifaDTO>> updateTarifa(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody TarifaDTO tarifaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tarifa : {}, {}", id, tarifaDTO);
        if (tarifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tarifaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tarifaService
                    .update(tarifaDTO)
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
     * {@code PATCH  /tarifas/:id} : Partial updates given fields of an existing tarifa, field will ignore if it is null
     *
     * @param id the id of the tarifaDTO to save.
     * @param tarifaDTO the tarifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarifaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tarifas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TarifaDTO>> partialUpdateTarifa(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody TarifaDTO tarifaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tarifa partially : {}, {}", id, tarifaDTO);
        if (tarifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tarifaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TarifaDTO> result = tarifaService.partialUpdate(tarifaDTO);

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
     * {@code GET  /tarifas} : get all the tarifas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tarifas in body.
     */
    @GetMapping("/tarifas")
    public Mono<ResponseEntity<List<TarifaDTO>>> getAllTarifas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Tarifas");
        return tarifaService
            .countAll()
            .zipWith(tarifaService.findAll(pageable).collectList())
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
     * {@code GET  /tarifas/:id} : get the "id" tarifa.
     *
     * @param id the id of the tarifaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarifaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tarifas/{id}")
    public Mono<ResponseEntity<TarifaDTO>> getTarifa(@PathVariable String id) {
        log.debug("REST request to get Tarifa : {}", id);
        Mono<TarifaDTO> tarifaDTO = tarifaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarifaDTO);
    }

    /**
     * {@code DELETE  /tarifas/:id} : delete the "id" tarifa.
     *
     * @param id the id of the tarifaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tarifas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTarifa(@PathVariable String id) {
        log.debug("REST request to delete Tarifa : {}", id);
        return tarifaService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
