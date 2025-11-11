package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.DetalleVenta;
import sistemacine.repository.DetalleVentaRepository;
import sistemacine.service.dto.DetalleVentaDTO;
import sistemacine.service.mapper.DetalleVentaMapper;

/**
 * Service Implementation for managing {@link DetalleVenta}.
 */
@Service
public class DetalleVentaService {

    private final Logger log = LoggerFactory.getLogger(DetalleVentaService.class);

    private final DetalleVentaRepository detalleVentaRepository;

    private final DetalleVentaMapper detalleVentaMapper;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository, DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    /**
     * Save a detalleVenta.
     *
     * @param detalleVentaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DetalleVentaDTO> save(DetalleVentaDTO detalleVentaDTO) {
        log.debug("Request to save DetalleVenta : {}", detalleVentaDTO);
        return detalleVentaRepository.save(detalleVentaMapper.toEntity(detalleVentaDTO)).map(detalleVentaMapper::toDto);
    }

    /**
     * Update a detalleVenta.
     *
     * @param detalleVentaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DetalleVentaDTO> update(DetalleVentaDTO detalleVentaDTO) {
        log.debug("Request to save DetalleVenta : {}", detalleVentaDTO);
        return detalleVentaRepository.save(detalleVentaMapper.toEntity(detalleVentaDTO)).map(detalleVentaMapper::toDto);
    }

    /**
     * Partially update a detalleVenta.
     *
     * @param detalleVentaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DetalleVentaDTO> partialUpdate(DetalleVentaDTO detalleVentaDTO) {
        log.debug("Request to partially update DetalleVenta : {}", detalleVentaDTO);

        return detalleVentaRepository
            .findById(detalleVentaDTO.getId())
            .map(existingDetalleVenta -> {
                detalleVentaMapper.partialUpdate(existingDetalleVenta, detalleVentaDTO);

                return existingDetalleVenta;
            })
            .flatMap(detalleVentaRepository::save)
            .map(detalleVentaMapper::toDto);
    }

    /**
     * Get all the detalleVentas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<DetalleVentaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DetalleVentas");
        return detalleVentaRepository.findAllBy(pageable).map(detalleVentaMapper::toDto);
    }

    /**
     * Returns the number of detalleVentas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return detalleVentaRepository.count();
    }

    /**
     * Get one detalleVenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<DetalleVentaDTO> findOne(String id) {
        log.debug("Request to get DetalleVenta : {}", id);
        return detalleVentaRepository.findById(id).map(detalleVentaMapper::toDto);
    }

    /**
     * Delete the detalleVenta by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete DetalleVenta : {}", id);
        return detalleVentaRepository.deleteById(id);
    }
}
