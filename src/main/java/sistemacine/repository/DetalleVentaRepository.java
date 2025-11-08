package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.DetalleVenta;

/**
 * Spring Data SQL reactive repository for the DetalleVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleVentaRepository extends ReactiveCrudRepository<DetalleVenta, Long>, DetalleVentaRepositoryInternal {
    Flux<DetalleVenta> findAllBy(Pageable pageable);

    @Query("SELECT * FROM detalle_venta entity WHERE entity.funcion_id = :id")
    Flux<DetalleVenta> findByFuncion(Long id);

    @Query("SELECT * FROM detalle_venta entity WHERE entity.funcion_id IS NULL")
    Flux<DetalleVenta> findAllWhereFuncionIsNull();

    @Query("SELECT * FROM detalle_venta entity WHERE entity.venta_id = :id")
    Flux<DetalleVenta> findByVenta(Long id);

    @Query("SELECT * FROM detalle_venta entity WHERE entity.venta_id IS NULL")
    Flux<DetalleVenta> findAllWhereVentaIsNull();

    @Override
    <S extends DetalleVenta> Mono<S> save(S entity);

    @Override
    Flux<DetalleVenta> findAll();

    @Override
    Mono<DetalleVenta> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DetalleVentaRepositoryInternal {
    <S extends DetalleVenta> Mono<S> save(S entity);

    Flux<DetalleVenta> findAllBy(Pageable pageable);

    Flux<DetalleVenta> findAll();

    Mono<DetalleVenta> findById(Long id);

    Flux<DetalleVenta> findAllBy(Pageable pageable, Criteria criteria);
}
