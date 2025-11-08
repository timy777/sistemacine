package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Reporte;
import sistemacine.repository.ReporteRepository;
import sistemacine.service.dto.ReporteDTO;
import sistemacine.service.mapper.ReporteMapper;

/**
 * Service Implementation for managing {@link Reporte}.
 */
@Service
@Transactional
public class ReporteService {

    private final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;

    private final ReporteMapper reporteMapper;

    public ReporteService(ReporteRepository reporteRepository, ReporteMapper reporteMapper) {
        this.reporteRepository = reporteRepository;
        this.reporteMapper = reporteMapper;
    }

    /**
     * Save a reporte.
     *
     * @param reporteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReporteDTO> save(ReporteDTO reporteDTO) {
        log.debug("Request to save Reporte : {}", reporteDTO);
        return reporteRepository.save(reporteMapper.toEntity(reporteDTO)).map(reporteMapper::toDto);
    }

    /**
     * Update a reporte.
     *
     * @param reporteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReporteDTO> update(ReporteDTO reporteDTO) {
        log.debug("Request to save Reporte : {}", reporteDTO);
        return reporteRepository.save(reporteMapper.toEntity(reporteDTO)).map(reporteMapper::toDto);
    }

    /**
     * Partially update a reporte.
     *
     * @param reporteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReporteDTO> partialUpdate(ReporteDTO reporteDTO) {
        log.debug("Request to partially update Reporte : {}", reporteDTO);

        return reporteRepository
            .findById(reporteDTO.getId())
            .map(existingReporte -> {
                reporteMapper.partialUpdate(existingReporte, reporteDTO);

                return existingReporte;
            })
            .flatMap(reporteRepository::save)
            .map(reporteMapper::toDto);
    }

    /**
     * Get all the reportes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReporteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reportes");
        return reporteRepository.findAllBy(pageable).map(reporteMapper::toDto);
    }

    /**
     * Get all the reportes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ReporteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reporteRepository.findAllWithEagerRelationships(pageable).map(reporteMapper::toDto);
    }

    /**
     * Returns the number of reportes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reporteRepository.count();
    }

    /**
     * Get one reporte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReporteDTO> findOne(Long id) {
        log.debug("Request to get Reporte : {}", id);
        return reporteRepository.findOneWithEagerRelationships(id).map(reporteMapper::toDto);
    }

    /**
     * Delete the reporte by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Reporte : {}", id);
        return reporteRepository.deleteById(id);
    }
}
