package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sistemacine.domain.Genero;

/**
 * Spring Data MongoDB reactive repository for the Genero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneroRepository extends ReactiveMongoRepository<Genero, String> {
    Flux<Genero> findAllBy(Pageable pageable);
}
