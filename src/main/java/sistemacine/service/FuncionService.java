package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Funcion;
import sistemacine.repository.FuncionRepository;
import sistemacine.service.dto.FuncionDTO;
import sistemacine.service.mapper.FuncionMapper;

/**
 * Service Implementation for managing {@link Funcion}.
 */
@Service
public class FuncionService {

    private final Logger log = LoggerFactory.getLogger(FuncionService.class);

    private final FuncionRepository funcionRepository;

    private final FuncionMapper funcionMapper;

    public FuncionService(FuncionRepository funcionRepository, FuncionMapper funcionMapper) {
        this.funcionRepository = funcionRepository;
        this.funcionMapper = funcionMapper;
    }

    /**
     * Save a funcion.
     *
     * @param funcionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FuncionDTO> save(FuncionDTO funcionDTO) {
        log.debug("Request to save Funcion : {}", funcionDTO);
        return funcionRepository.save(funcionMapper.toEntity(funcionDTO)).map(funcionMapper::toDto);
    }

    /**
     * Update a funcion.
     *
     * @param funcionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FuncionDTO> update(FuncionDTO funcionDTO) {
        log.debug("Request to save Funcion : {}", funcionDTO);
        return funcionRepository.save(funcionMapper.toEntity(funcionDTO)).map(funcionMapper::toDto);
    }

    /**
     * Partially update a funcion.
     *
     * @param funcionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FuncionDTO> partialUpdate(FuncionDTO funcionDTO) {
        log.debug("Request to partially update Funcion : {}", funcionDTO);

        return funcionRepository
            .findById(funcionDTO.getId())
            .map(existingFuncion -> {
                funcionMapper.partialUpdate(existingFuncion, funcionDTO);

                return existingFuncion;
            })
            .flatMap(funcionRepository::save)
            .map(funcionMapper::toDto);
    }

    /**
     * Get all the funcions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<FuncionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Funcions");
        return funcionRepository.findAllBy(pageable).map(funcionMapper::toDto);
    }

    /**
     * Get all the funcions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<FuncionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return funcionRepository.findAllWithEagerRelationships(pageable).map(funcionMapper::toDto);
    }

    /**
     * Returns the number of funcions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return funcionRepository.count();
    }

    /**
     * Get one funcion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<FuncionDTO> findOne(String id) {
        log.debug("Request to get Funcion : {}", id);
        return funcionRepository.findOneWithEagerRelationships(id).map(funcionMapper::toDto);
    }

    /**
     * Delete the funcion by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Funcion : {}", id);
        return funcionRepository.deleteById(id);
    }
}
