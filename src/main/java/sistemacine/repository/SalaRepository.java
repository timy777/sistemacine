package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sistemacine.domain.Sala;

/**
 * Spring Data MongoDB reactive repository for the Sala entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaRepository extends ReactiveMongoRepository<Sala, String> {
    Flux<Sala> findAllBy(Pageable pageable);
}
