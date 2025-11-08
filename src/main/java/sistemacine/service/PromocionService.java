package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Promocion;
import sistemacine.repository.PromocionRepository;
import sistemacine.service.dto.PromocionDTO;
import sistemacine.service.mapper.PromocionMapper;

/**
 * Service Implementation for managing {@link Promocion}.
 */
@Service
@Transactional
public class PromocionService {

    private final Logger log = LoggerFactory.getLogger(PromocionService.class);

    private final PromocionRepository promocionRepository;

    private final PromocionMapper promocionMapper;

    public PromocionService(PromocionRepository promocionRepository, PromocionMapper promocionMapper) {
        this.promocionRepository = promocionRepository;
        this.promocionMapper = promocionMapper;
    }

    /**
     * Save a promocion.
     *
     * @param promocionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PromocionDTO> save(PromocionDTO promocionDTO) {
        log.debug("Request to save Promocion : {}", promocionDTO);
        return promocionRepository.save(promocionMapper.toEntity(promocionDTO)).map(promocionMapper::toDto);
    }

    /**
     * Update a promocion.
     *
     * @param promocionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PromocionDTO> update(PromocionDTO promocionDTO) {
        log.debug("Request to save Promocion : {}", promocionDTO);
        return promocionRepository.save(promocionMapper.toEntity(promocionDTO)).map(promocionMapper::toDto);
    }

    /**
     * Partially update a promocion.
     *
     * @param promocionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PromocionDTO> partialUpdate(PromocionDTO promocionDTO) {
        log.debug("Request to partially update Promocion : {}", promocionDTO);

        return promocionRepository
            .findById(promocionDTO.getId())
            .map(existingPromocion -> {
                promocionMapper.partialUpdate(existingPromocion, promocionDTO);

                return existingPromocion;
            })
            .flatMap(promocionRepository::save)
            .map(promocionMapper::toDto);
    }

    /**
     * Get all the promocions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PromocionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promocions");
        return promocionRepository.findAllBy(pageable).map(promocionMapper::toDto);
    }

    /**
     * Get all the promocions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PromocionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return promocionRepository.findAllWithEagerRelationships(pageable).map(promocionMapper::toDto);
    }

    /**
     * Returns the number of promocions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return promocionRepository.count();
    }

    /**
     * Get one promocion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PromocionDTO> findOne(Long id) {
        log.debug("Request to get Promocion : {}", id);
        return promocionRepository.findOneWithEagerRelationships(id).map(promocionMapper::toDto);
    }

    /**
     * Delete the promocion by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Promocion : {}", id);
        return promocionRepository.deleteById(id);
    }
}
