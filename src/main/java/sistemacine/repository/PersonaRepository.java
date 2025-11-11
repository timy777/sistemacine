package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sistemacine.domain.Persona;

/**
 * Spring Data MongoDB reactive repository for the Persona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonaRepository extends ReactiveMongoRepository<Persona, String> {
    Flux<Persona> findAllBy(Pageable pageable);
}
