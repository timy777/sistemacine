package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Sala;
import sistemacine.service.dto.SalaDTO;

/**
 * Mapper for the entity {@link Sala} and its DTO {@link SalaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaMapper extends EntityMapper<SalaDTO, Sala> {}
