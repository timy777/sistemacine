package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Venta;
import sistemacine.repository.VentaRepository;
import sistemacine.service.dto.VentaDTO;
import sistemacine.service.mapper.VentaMapper;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;

    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository, VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
    }

    /**
     * Save a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VentaDTO> save(VentaDTO ventaDTO) {
        log.debug("Request to save Venta : {}", ventaDTO);
        return ventaRepository.save(ventaMapper.toEntity(ventaDTO)).map(ventaMapper::toDto);
    }

    /**
     * Update a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VentaDTO> update(VentaDTO ventaDTO) {
        log.debug("Request to save Venta : {}", ventaDTO);
        return ventaRepository.save(ventaMapper.toEntity(ventaDTO)).map(ventaMapper::toDto);
    }

    /**
     * Partially update a venta.
     *
     * @param ventaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        log.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepository
            .findById(ventaDTO.getId())
            .map(existingVenta -> {
                ventaMapper.partialUpdate(existingVenta, ventaDTO);

                return existingVenta;
            })
            .flatMap(ventaRepository::save)
            .map(ventaMapper::toDto);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<VentaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAllBy(pageable).map(ventaMapper::toDto);
    }

    /**
     * Get all the ventas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<VentaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable).map(ventaMapper::toDto);
    }

    /**
     * Returns the number of ventas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return ventaRepository.count();
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<VentaDTO> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id).map(ventaMapper::toDto);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        return ventaRepository.deleteById(id);
    }
}
