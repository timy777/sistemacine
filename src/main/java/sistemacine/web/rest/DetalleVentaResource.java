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
import sistemacine.repository.DetalleVentaRepository;
import sistemacine.service.DetalleVentaService;
import sistemacine.service.dto.DetalleVentaDTO;
import sistemacine.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sistemacine.domain.DetalleVenta}.
 */
@RestController
@RequestMapping("/api")
public class DetalleVentaResource {

    private final Logger log = LoggerFactory.getLogger(DetalleVentaResource.class);

    private static final String ENTITY_NAME = "detalleVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleVentaService detalleVentaService;

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaResource(DetalleVentaService detalleVentaService, DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaService = detalleVentaService;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    /**
     * {@code POST  /detalle-ventas} : Create a new detalleVenta.
     *
     * @param detalleVentaDTO the detalleVentaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleVentaDTO, or with status {@code 400 (Bad Request)} if the detalleVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detalle-ventas")
    public Mono<ResponseEntity<DetalleVentaDTO>> createDetalleVenta(@Valid @RequestBody DetalleVentaDTO detalleVentaDTO)
        throws URISyntaxException {
        log.debug("REST request to save DetalleVenta : {}", detalleVentaDTO);
        if (detalleVentaDTO.getId() != null) {
            throw new BadRequestAlertException("A new detalleVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return detalleVentaService
            .save(detalleVentaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/detalle-ventas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /detalle-ventas/:id} : Updates an existing detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to save.
     * @param detalleVentaDTO the detalleVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVentaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleVentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detalle-ventas/{id}")
    public Mono<ResponseEntity<DetalleVentaDTO>> updateDetalleVenta(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DetalleVentaDTO detalleVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DetalleVenta : {}, {}", id, detalleVentaDTO);
        if (detalleVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleVentaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return detalleVentaService
                    .update(detalleVentaDTO)
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
     * {@code PATCH  /detalle-ventas/:id} : Partial updates given fields of an existing detalleVenta, field will ignore if it is null
     *
     * @param id the id of the detalleVentaDTO to save.
     * @param detalleVentaDTO the detalleVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleVentaDTO,
     * or with status {@code 400 (Bad Request)} if the detalleVentaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detalleVentaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detalle-ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DetalleVentaDTO>> partialUpdateDetalleVenta(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody DetalleVentaDTO detalleVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetalleVenta partially : {}, {}", id, detalleVentaDTO);
        if (detalleVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return detalleVentaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DetalleVentaDTO> result = detalleVentaService.partialUpdate(detalleVentaDTO);

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
     * {@code GET  /detalle-ventas} : get all the detalleVentas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleVentas in body.
     */
    @GetMapping("/detalle-ventas")
    public Mono<ResponseEntity<List<DetalleVentaDTO>>> getAllDetalleVentas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DetalleVentas");
        return detalleVentaService
            .countAll()
            .zipWith(detalleVentaService.findAll(pageable).collectList())
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
     * {@code GET  /detalle-ventas/:id} : get the "id" detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleVentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalle-ventas/{id}")
    public Mono<ResponseEntity<DetalleVentaDTO>> getDetalleVenta(@PathVariable String id) {
        log.debug("REST request to get DetalleVenta : {}", id);
        Mono<DetalleVentaDTO> detalleVentaDTO = detalleVentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleVentaDTO);
    }

    /**
     * {@code DELETE  /detalle-ventas/:id} : delete the "id" detalleVenta.
     *
     * @param id the id of the detalleVentaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detalle-ventas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDetalleVenta(@PathVariable String id) {
        log.debug("REST request to delete DetalleVenta : {}", id);
        return detalleVentaService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
            );
    }
}
