package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Genero;
import sistemacine.repository.GeneroRepository;
import sistemacine.service.dto.GeneroDTO;
import sistemacine.service.mapper.GeneroMapper;

/**
 * Service Implementation for managing {@link Genero}.
 */
@Service
public class GeneroService {

    private final Logger log = LoggerFactory.getLogger(GeneroService.class);

    private final GeneroRepository generoRepository;

    private final GeneroMapper generoMapper;

    public GeneroService(GeneroRepository generoRepository, GeneroMapper generoMapper) {
        this.generoRepository = generoRepository;
        this.generoMapper = generoMapper;
    }

    /**
     * Save a genero.
     *
     * @param generoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GeneroDTO> save(GeneroDTO generoDTO) {
        log.debug("Request to save Genero : {}", generoDTO);
        return generoRepository.save(generoMapper.toEntity(generoDTO)).map(generoMapper::toDto);
    }

    /**
     * Update a genero.
     *
     * @param generoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GeneroDTO> update(GeneroDTO generoDTO) {
        log.debug("Request to save Genero : {}", generoDTO);
        return generoRepository.save(generoMapper.toEntity(generoDTO)).map(generoMapper::toDto);
    }

    /**
     * Partially update a genero.
     *
     * @param generoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GeneroDTO> partialUpdate(GeneroDTO generoDTO) {
        log.debug("Request to partially update Genero : {}", generoDTO);

        return generoRepository
            .findById(generoDTO.getId())
            .map(existingGenero -> {
                generoMapper.partialUpdate(existingGenero, generoDTO);

                return existingGenero;
            })
            .flatMap(generoRepository::save)
            .map(generoMapper::toDto);
    }

    /**
     * Get all the generos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<GeneroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Generos");
        return generoRepository.findAllBy(pageable).map(generoMapper::toDto);
    }

    /**
     * Returns the number of generos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return generoRepository.count();
    }

    /**
     * Get one genero by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<GeneroDTO> findOne(String id) {
        log.debug("Request to get Genero : {}", id);
        return generoRepository.findById(id).map(generoMapper::toDto);
    }

    /**
     * Delete the genero by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Genero : {}", id);
        return generoRepository.deleteById(id);
    }
}
