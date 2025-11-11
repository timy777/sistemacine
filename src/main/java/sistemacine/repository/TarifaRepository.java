package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sistemacine.domain.Tarifa;

/**
 * Spring Data MongoDB reactive repository for the Tarifa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifaRepository extends ReactiveMongoRepository<Tarifa, String> {
    Flux<Tarifa> findAllBy(Pageable pageable);
}
