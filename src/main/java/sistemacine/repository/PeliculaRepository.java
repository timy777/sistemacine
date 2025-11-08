package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Pelicula;

/**
 * Spring Data SQL reactive repository for the Pelicula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeliculaRepository extends ReactiveCrudRepository<Pelicula, Long>, PeliculaRepositoryInternal {
    Flux<Pelicula> findAllBy(Pageable pageable);

    @Override
    Mono<Pelicula> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Pelicula> findAllWithEagerRelationships();

    @Override
    Flux<Pelicula> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM pelicula entity WHERE entity.genero_id = :id")
    Flux<Pelicula> findByGenero(Long id);

    @Query("SELECT * FROM pelicula entity WHERE entity.genero_id IS NULL")
    Flux<Pelicula> findAllWhereGeneroIsNull();

    @Override
    <S extends Pelicula> Mono<S> save(S entity);

    @Override
    Flux<Pelicula> findAll();

    @Override
    Mono<Pelicula> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PeliculaRepositoryInternal {
    <S extends Pelicula> Mono<S> save(S entity);

    Flux<Pelicula> findAllBy(Pageable pageable);

    Flux<Pelicula> findAll();

    Mono<Pelicula> findById(Long id);

    Flux<Pelicula> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Pelicula> findOneWithEagerRelationships(Long id);

    Flux<Pelicula> findAllWithEagerRelationships();

    Flux<Pelicula> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
