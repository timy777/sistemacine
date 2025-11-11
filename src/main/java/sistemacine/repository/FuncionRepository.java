package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Funcion;

/**
 * Spring Data MongoDB reactive repository for the Funcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncionRepository extends ReactiveMongoRepository<Funcion, String> {
    Flux<Funcion> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Funcion> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Funcion> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Funcion> findOneWithEagerRelationships(String id);
}
