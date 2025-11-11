package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Venta;

/**
 * Spring Data MongoDB reactive repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends ReactiveMongoRepository<Venta, String> {
    Flux<Venta> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Venta> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Venta> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Venta> findOneWithEagerRelationships(String id);
}
