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
import sistemacine.repository.PromocionRepository;
import sistemacine.service.PromocionService;
import sistemacine.service.dto.PromocionDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.Promocion}.
 */
@RestController
@RequestMapping("/api")
public class PromocionResource {

    private final Logger log = LoggerFactory.getLogger(PromocionResource.class);

    private static final String ENTITY_NAME = "promocion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromocionService promocionService;

    private final PromocionRepository promocionRepository;

    public PromocionResource(PromocionService promocionService, PromocionRepository promocionRepository) {
        this.promocionService = promocionService;
        this.promocionRepository = promocionRepository;
    }

    /**
     * {@code POST  /promocions} : Create a new promocion.
     *
     * @param promocionDTO the promocionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promocionDTO, or with status {@code 400 (Bad Request)} if the promocion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promocions")
    public Mono<ResponseEntity<PromocionDTO>> createPromocion(@Valid @RequestBody PromocionDTO promocionDTO) throws URISyntaxException {
        log.debug("REST request to save Promocion : {}", promocionDTO);
        if (promocionDTO.getId() != null) {
            throw new BadRequestAlertException("A new promocion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return promocionService
            .save(promocionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/promocions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /promocions/:id} : Updates an existing promocion.
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promocions/{id}")
    public Mono<ResponseEntity<PromocionDTO>> updatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Promocion : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promocionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return promocionService
                    .update(promocionDTO)
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
     * {@code PATCH  /promocions/:id} : Partial updates given fields of an existing promocion, field will ignore if it is null
     *
     * @param id the id of the promocionDTO to save.
     * @param promocionDTO the promocionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocionDTO,
     * or with status {@code 400 (Bad Request)} if the promocionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the promocionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the promocionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promocions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PromocionDTO>> partialUpdatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PromocionDTO promocionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Promocion partially : {}, {}", id, promocionDTO);
        if (promocionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promocionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PromocionDTO> result = promocionService.partialUpdate(promocionDTO);

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
     * {@code GET  /promocions} : get all the promocions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promocions in body.
     */
    @GetMapping("/promocions")
    public Mono<ResponseEntity<List<PromocionDTO>>> getAllPromocions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Promocions");
        return promocionService
            .countAll()
            .zipWith(promocionService.findAll(pageable).collectList())
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
     * {@code GET  /promocions/:id} : get the "id" promocion.
     *
     * @param id the id of the promocionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promocionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promocions/{id}")
    public Mono<ResponseEntity<PromocionDTO>> getPromocion(@PathVariable Long id) {
        log.debug("REST request to get Promocion : {}", id);
        Mono<PromocionDTO> promocionDTO = promocionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promocionDTO);
    }

    /**
     * {@code DELETE  /promocions/:id} : delete the "id" promocion.
     *
     * @param id the id of the promocionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promocions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePromocion(@PathVariable Long id) {
        log.debug("REST request to delete Promocion : {}", id);
        return promocionService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
