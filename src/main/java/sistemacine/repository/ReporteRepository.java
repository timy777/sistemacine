package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Reporte;

/**
 * Spring Data MongoDB reactive repository for the Reporte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReporteRepository extends ReactiveMongoRepository<Reporte, String> {
    Flux<Reporte> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Reporte> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Reporte> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Reporte> findOneWithEagerRelationships(String id);
}
