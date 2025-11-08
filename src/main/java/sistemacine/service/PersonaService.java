package sistemacine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Persona;
import sistemacine.repository.PersonaRepository;
import sistemacine.service.dto.PersonaDTO;
import sistemacine.service.mapper.PersonaMapper;

/**
 * Service Implementation for managing {@link Persona}.
 */
@Service
@Transactional
public class PersonaService {

    private final Logger log = LoggerFactory.getLogger(PersonaService.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    /**
     * Save a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PersonaDTO> save(PersonaDTO personaDTO) {
        log.debug("Request to save Persona : {}", personaDTO);
        return personaRepository.save(personaMapper.toEntity(personaDTO)).map(personaMapper::toDto);
    }

    /**
     * Update a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PersonaDTO> update(PersonaDTO personaDTO) {
        log.debug("Request to save Persona : {}", personaDTO);
        return personaRepository.save(personaMapper.toEntity(personaDTO)).map(personaMapper::toDto);
    }

    /**
     * Partially update a persona.
     *
     * @param personaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PersonaDTO> partialUpdate(PersonaDTO personaDTO) {
        log.debug("Request to partially update Persona : {}", personaDTO);

        return personaRepository
            .findById(personaDTO.getId())
            .map(existingPersona -> {
                personaMapper.partialUpdate(existingPersona, personaDTO);

                return existingPersona;
            })
            .flatMap(personaRepository::save)
            .map(personaMapper::toDto);
    }

    /**
     * Get all the personas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PersonaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personas");
        return personaRepository.findAllBy(pageable).map(personaMapper::toDto);
    }

    /**
     * Returns the number of personas available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return personaRepository.count();
    }

    /**
     * Get one persona by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PersonaDTO> findOne(Long id) {
        log.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id).map(personaMapper::toDto);
    }

    /**
     * Delete the persona by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Persona : {}", id);
        return personaRepository.deleteById(id);
    }
}
