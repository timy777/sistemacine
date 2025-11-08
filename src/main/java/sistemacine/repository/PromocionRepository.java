package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Promocion;

/**
 * Spring Data SQL reactive repository for the Promocion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromocionRepository extends ReactiveCrudRepository<Promocion, Long>, PromocionRepositoryInternal {
    Flux<Promocion> findAllBy(Pageable pageable);

    @Override
    Mono<Promocion> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Promocion> findAllWithEagerRelationships();

    @Override
    Flux<Promocion> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM promocion entity JOIN rel_promocion__peliculas joinTable ON entity.id = joinTable.peliculas_id WHERE joinTable.peliculas_id = :id"
    )
    Flux<Promocion> findByPeliculas(Long id);

    @Override
    <S extends Promocion> Mono<S> save(S entity);

    @Override
    Flux<Promocion> findAll();

    @Override
    Mono<Promocion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PromocionRepositoryInternal {
    <S extends Promocion> Mono<S> save(S entity);

    Flux<Promocion> findAllBy(Pageable pageable);

    Flux<Promocion> findAll();

    Mono<Promocion> findById(Long id);

    Flux<Promocion> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Promocion> findOneWithEagerRelationships(Long id);

    Flux<Promocion> findAllWithEagerRelationships();

    Flux<Promocion> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
