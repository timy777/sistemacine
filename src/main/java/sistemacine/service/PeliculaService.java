package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Pelicula;
import sistemacine.repository.PeliculaRepository;
import sistemacine.service.dto.PeliculaDTO;
import sistemacine.service.mapper.PeliculaMapper;

/**
 * Service Implementation for managing {@link Pelicula}.
 */
@Service
@Transactional
public class PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaService.class);

    private final PeliculaRepository peliculaRepository;

    private final PeliculaMapper peliculaMapper;

    public PeliculaService(PeliculaRepository peliculaRepository, PeliculaMapper peliculaMapper) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
    }

    /**
     * Save a pelicula.
     *
     * @param peliculaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PeliculaDTO> save(PeliculaDTO peliculaDTO) {
        log.debug("Request to save Pelicula : {}", peliculaDTO);
        return peliculaRepository.save(peliculaMapper.toEntity(peliculaDTO)).map(peliculaMapper::toDto);
    }

    /**
     * Update a pelicula.
     *
     * @param peliculaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PeliculaDTO> update(PeliculaDTO peliculaDTO) {
        log.debug("Request to save Pelicula : {}", peliculaDTO);
        return peliculaRepository.save(peliculaMapper.toEntity(peliculaDTO)).map(peliculaMapper::toDto);
    }

    /**
     * Partially update a pelicula.
     *
     * @param peliculaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PeliculaDTO> partialUpdate(PeliculaDTO peliculaDTO) {
        log.debug("Request to partially update Pelicula : {}", peliculaDTO);

        return peliculaRepository
            .findById(peliculaDTO.getId())
            .map(existingPelicula -> {
                peliculaMapper.partialUpdate(existingPelicula, peliculaDTO);

                return existingPelicula;
            })
            .flatMap(peliculaRepository::save)
            .map(peliculaMapper::toDto);
    }

    /**
     * Get all the peliculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PeliculaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAllBy(pageable).map(peliculaMapper::toDto);
    }

    /**
     * Get all the peliculas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PeliculaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return peliculaRepository.findAllWithEagerRelationships(pageable).map(peliculaMapper::toDto);
    }

    /**
     * Returns the number of peliculas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return peliculaRepository.count();
    }

    /**
     * Get one pelicula by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PeliculaDTO> findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findOneWithEagerRelationships(id).map(peliculaMapper::toDto);
    }

    /**
     * Delete the pelicula by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        return peliculaRepository.deleteById(id);
    }
}
