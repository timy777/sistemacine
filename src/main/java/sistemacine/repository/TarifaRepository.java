package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Tarifa;

/**
 * Spring Data SQL reactive repository for the Tarifa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifaRepository extends ReactiveCrudRepository<Tarifa, Long>, TarifaRepositoryInternal {
    Flux<Tarifa> findAllBy(Pageable pageable);

    @Override
    <S extends Tarifa> Mono<S> save(S entity);

    @Override
    Flux<Tarifa> findAll();

    @Override
    Mono<Tarifa> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TarifaRepositoryInternal {
    <S extends Tarifa> Mono<S> save(S entity);

    Flux<Tarifa> findAllBy(Pageable pageable);

    Flux<Tarifa> findAll();

    Mono<Tarifa> findById(Long id);

    Flux<Tarifa> findAllBy(Pageable pageable, Criteria criteria);
}
