package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Venta;

/**
 * Spring Data SQL reactive repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends ReactiveCrudRepository<Venta, Long>, VentaRepositoryInternal {
    Flux<Venta> findAllBy(Pageable pageable);

    @Override
    Mono<Venta> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Venta> findAllWithEagerRelationships();

    @Override
    Flux<Venta> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM venta entity WHERE entity.cliente_id = :id")
    Flux<Venta> findByCliente(Long id);

    @Query("SELECT * FROM venta entity WHERE entity.cliente_id IS NULL")
    Flux<Venta> findAllWhereClienteIsNull();

    @Query("SELECT * FROM venta entity WHERE entity.vendedor_id = :id")
    Flux<Venta> findByVendedor(Long id);

    @Query("SELECT * FROM venta entity WHERE entity.vendedor_id IS NULL")
    Flux<Venta> findAllWhereVendedorIsNull();

    @Override
    <S extends Venta> Mono<S> save(S entity);

    @Override
    Flux<Venta> findAll();

    @Override
    Mono<Venta> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VentaRepositoryInternal {
    <S extends Venta> Mono<S> save(S entity);

    Flux<Venta> findAllBy(Pageable pageable);

    Flux<Venta> findAll();

    Mono<Venta> findById(Long id);

    Flux<Venta> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Venta> findOneWithEagerRelationships(Long id);

    Flux<Venta> findAllWithEagerRelationships();

    Flux<Venta> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
