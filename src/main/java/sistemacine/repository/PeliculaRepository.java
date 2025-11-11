package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Pelicula;

/**
 * Spring Data MongoDB reactive repository for the Pelicula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeliculaRepository extends ReactiveMongoRepository<Pelicula, String> {
    Flux<Pelicula> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Pelicula> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Pelicula> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Pelicula> findOneWithEagerRelationships(String id);
}
