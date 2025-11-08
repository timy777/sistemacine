package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Sala;

/**
 * Spring Data SQL reactive repository for the Sala entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaRepository extends ReactiveCrudRepository<Sala, Long>, SalaRepositoryInternal {
    Flux<Sala> findAllBy(Pageable pageable);

    @Override
    <S extends Sala> Mono<S> save(S entity);

    @Override
    Flux<Sala> findAll();

    @Override
    Mono<Sala> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SalaRepositoryInternal {
    <S extends Sala> Mono<S> save(S entity);

    Flux<Sala> findAllBy(Pageable pageable);

    Flux<Sala> findAll();

    Mono<Sala> findById(Long id);

    Flux<Sala> findAllBy(Pageable pageable, Criteria criteria);
}
