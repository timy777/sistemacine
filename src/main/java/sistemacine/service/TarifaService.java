package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Tarifa;
import sistemacine.repository.TarifaRepository;
import sistemacine.service.dto.TarifaDTO;
import sistemacine.service.mapper.TarifaMapper;

/**
 * Service Implementation for managing {@link Tarifa}.
 */
@Service
@Transactional
public class TarifaService {

    private final Logger log = LoggerFactory.getLogger(TarifaService.class);

    private final TarifaRepository tarifaRepository;

    private final TarifaMapper tarifaMapper;

    public TarifaService(TarifaRepository tarifaRepository, TarifaMapper tarifaMapper) {
        this.tarifaRepository = tarifaRepository;
        this.tarifaMapper = tarifaMapper;
    }

    /**
     * Save a tarifa.
     *
     * @param tarifaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TarifaDTO> save(TarifaDTO tarifaDTO) {
        log.debug("Request to save Tarifa : {}", tarifaDTO);
        return tarifaRepository.save(tarifaMapper.toEntity(tarifaDTO)).map(tarifaMapper::toDto);
    }

    /**
     * Update a tarifa.
     *
     * @param tarifaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TarifaDTO> update(TarifaDTO tarifaDTO) {
        log.debug("Request to save Tarifa : {}", tarifaDTO);
        return tarifaRepository.save(tarifaMapper.toEntity(tarifaDTO)).map(tarifaMapper::toDto);
    }

    /**
     * Partially update a tarifa.
     *
     * @param tarifaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TarifaDTO> partialUpdate(TarifaDTO tarifaDTO) {
        log.debug("Request to partially update Tarifa : {}", tarifaDTO);

        return tarifaRepository
            .findById(tarifaDTO.getId())
            .map(existingTarifa -> {
                tarifaMapper.partialUpdate(existingTarifa, tarifaDTO);

                return existingTarifa;
            })
            .flatMap(tarifaRepository::save)
            .map(tarifaMapper::toDto);
    }

    /**
     * Get all the tarifas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TarifaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tarifas");
        return tarifaRepository.findAllBy(pageable).map(tarifaMapper::toDto);
    }

    /**
     * Returns the number of tarifas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return tarifaRepository.count();
    }

    /**
     * Get one tarifa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TarifaDTO> findOne(Long id) {
        log.debug("Request to get Tarifa : {}", id);
        return tarifaRepository.findById(id).map(tarifaMapper::toDto);
    }

    /**
     * Delete the tarifa by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Tarifa : {}", id);
        return tarifaRepository.deleteById(id);
    }
}
