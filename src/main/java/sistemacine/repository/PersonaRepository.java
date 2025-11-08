package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sistemacine.domain.Persona;

/**
 * Spring Data SQL reactive repository for the Persona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonaRepository extends ReactiveCrudRepository<Persona, Long>, PersonaRepositoryInternal {
    Flux<Persona> findAllBy(Pageable pageable);

    @Override
    <S extends Persona> Mono<S> save(S entity);

    @Override
    Flux<Persona> findAll();

    @Override
    Mono<Persona> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PersonaRepositoryInternal {
    <S extends Persona> Mono<S> save(S entity);

    Flux<Persona> findAllBy(Pageable pageable);

    Flux<Persona> findAll();

    Mono<Persona> findById(Long id);

    Flux<Persona> findAllBy(Pageable pageable, Criteria criteria);
}
