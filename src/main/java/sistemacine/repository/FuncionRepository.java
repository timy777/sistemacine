package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Funcion;

/**
 * Spring Data SQL reactive repository for the Funcion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncionRepository extends ReactiveCrudRepository<Funcion, Long>, FuncionRepositoryInternal {
    Flux<Funcion> findAllBy(Pageable pageable);

    @Override
    Mono<Funcion> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Funcion> findAllWithEagerRelationships();

    @Override
    Flux<Funcion> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM funcion entity WHERE entity.sala_id = :id")
    Flux<Funcion> findBySala(Long id);

    @Query("SELECT * FROM funcion entity WHERE entity.sala_id IS NULL")
    Flux<Funcion> findAllWhereSalaIsNull();

    @Query("SELECT * FROM funcion entity WHERE entity.pelicula_id = :id")
    Flux<Funcion> findByPelicula(Long id);

    @Query("SELECT * FROM funcion entity WHERE entity.pelicula_id IS NULL")
    Flux<Funcion> findAllWherePeliculaIsNull();

    @Query("SELECT * FROM funcion entity WHERE entity.tarifa_id = :id")
    Flux<Funcion> findByTarifa(Long id);

    @Query("SELECT * FROM funcion entity WHERE entity.tarifa_id IS NULL")
    Flux<Funcion> findAllWhereTarifaIsNull();

    @Override
    <S extends Funcion> Mono<S> save(S entity);

    @Override
    Flux<Funcion> findAll();

    @Override
    Mono<Funcion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FuncionRepositoryInternal {
    <S extends Funcion> Mono<S> save(S entity);

    Flux<Funcion> findAllBy(Pageable pageable);

    Flux<Funcion> findAll();

    Mono<Funcion> findById(Long id);

    Flux<Funcion> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Funcion> findOneWithEagerRelationships(Long id);

    Flux<Funcion> findAllWithEagerRelationships();

    Flux<Funcion> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
