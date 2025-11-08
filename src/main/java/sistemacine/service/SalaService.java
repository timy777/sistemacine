package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Sala;
import sistemacine.repository.SalaRepository;
import sistemacine.service.dto.SalaDTO;
import sistemacine.service.mapper.SalaMapper;

/**
 * Service Implementation for managing {@link Sala}.
 */
@Service
@Transactional
public class SalaService {

    private final Logger log = LoggerFactory.getLogger(SalaService.class);

    private final SalaRepository salaRepository;

    private final SalaMapper salaMapper;

    public SalaService(SalaRepository salaRepository, SalaMapper salaMapper) {
        this.salaRepository = salaRepository;
        this.salaMapper = salaMapper;
    }

    /**
     * Save a sala.
     *
     * @param salaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SalaDTO> save(SalaDTO salaDTO) {
        log.debug("Request to save Sala : {}", salaDTO);
        return salaRepository.save(salaMapper.toEntity(salaDTO)).map(salaMapper::toDto);
    }

    /**
     * Update a sala.
     *
     * @param salaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SalaDTO> update(SalaDTO salaDTO) {
        log.debug("Request to save Sala : {}", salaDTO);
        return salaRepository.save(salaMapper.toEntity(salaDTO)).map(salaMapper::toDto);
    }

    /**
     * Partially update a sala.
     *
     * @param salaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SalaDTO> partialUpdate(SalaDTO salaDTO) {
        log.debug("Request to partially update Sala : {}", salaDTO);

        return salaRepository
            .findById(salaDTO.getId())
            .map(existingSala -> {
                salaMapper.partialUpdate(existingSala, salaDTO);

                return existingSala;
            })
            .flatMap(salaRepository::save)
            .map(salaMapper::toDto);
    }

    /**
     * Get all the salas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SalaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Salas");
        return salaRepository.findAllBy(pageable).map(salaMapper::toDto);
    }

    /**
     * Returns the number of salas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return salaRepository.count();
    }

    /**
     * Get one sala by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SalaDTO> findOne(Long id) {
        log.debug("Request to get Sala : {}", id);
        return salaRepository.findById(id).map(salaMapper::toDto);
    }

    /**
     * Delete the sala by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sala : {}", id);
        return salaRepository.deleteById(id);
    }
}
