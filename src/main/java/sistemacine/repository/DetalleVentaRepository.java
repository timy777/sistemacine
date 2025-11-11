package sistemacine.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import sistemacine.domain.DetalleVenta;

/**
 * Spring Data MongoDB reactive repository for the DetalleVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleVentaRepository extends ReactiveMongoRepository<DetalleVenta, String> {
    Flux<DetalleVenta> findAllBy(Pageable pageable);
}
