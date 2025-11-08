package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Persona;
import sistemacine.service.dto.PersonaDTO;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {}
