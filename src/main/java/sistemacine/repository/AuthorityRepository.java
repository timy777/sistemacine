package sistemacine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import sistemacine.domain.Authority;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {}
