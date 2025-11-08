package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Reporte;

/**
 * Spring Data SQL reactive repository for the Reporte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReporteRepository extends ReactiveCrudRepository<Reporte, Long>, ReporteRepositoryInternal {
    Flux<Reporte> findAllBy(Pageable pageable);

    @Override
    Mono<Reporte> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Reporte> findAllWithEagerRelationships();

    @Override
    Flux<Reporte> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM reporte entity WHERE entity.vendedor_id = :id")
    Flux<Reporte> findByVendedor(Long id);

    @Query("SELECT * FROM reporte entity WHERE entity.vendedor_id IS NULL")
    Flux<Reporte> findAllWhereVendedorIsNull();

    @Override
    <S extends Reporte> Mono<S> save(S entity);

    @Override
    Flux<Reporte> findAll();

    @Override
    Mono<Reporte> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ReporteRepositoryInternal {
    <S extends Reporte> Mono<S> save(S entity);

    Flux<Reporte> findAllBy(Pageable pageable);

    Flux<Reporte> findAll();

    Mono<Reporte> findById(Long id);

    Flux<Reporte> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Reporte> findOneWithEagerRelationships(Long id);

    Flux<Reporte> findAllWithEagerRelationships();

    Flux<Reporte> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
