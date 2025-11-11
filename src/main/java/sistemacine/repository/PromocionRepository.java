package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Promocion;

/**
 * Spring Data MongoDB reactive repository for the Promocion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromocionRepository extends ReactiveMongoRepository<Promocion, String> {
    Flux<Promocion> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Promocion> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Promocion> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Promocion> findOneWithEagerRelationships(String id);
}
