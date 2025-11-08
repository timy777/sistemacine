package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Genero;

/**
 * Spring Data SQL reactive repository for the Genero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneroRepository extends ReactiveCrudRepository<Genero, Long>, GeneroRepositoryInternal {
    Flux<Genero> findAllBy(Pageable pageable);

    @Override
    <S extends Genero> Mono<S> save(S entity);

    @Override
    Flux<Genero> findAll();

    @Override
    Mono<Genero> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GeneroRepositoryInternal {
    <S extends Genero> Mono<S> save(S entity);

    Flux<Genero> findAllBy(Pageable pageable);

    Flux<Genero> findAll();

    Mono<Genero> findById(Long id);

    Flux<Genero> findAllBy(Pageable pageable, Criteria criteria);
}
