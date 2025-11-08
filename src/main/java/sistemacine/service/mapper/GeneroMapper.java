package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Genero;
import sistemacine.service.dto.GeneroDTO;

/**
 * Mapper for the entity {@link Genero} and its DTO {@link GeneroDTO}.
 */
@Mapper(componentModel = "spring")
public interface GeneroMapper extends EntityMapper<GeneroDTO, Genero> {}
