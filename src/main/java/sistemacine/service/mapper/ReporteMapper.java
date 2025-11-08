package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Persona;
import sistemacine.domain.Reporte;
import sistemacine.service.dto.PersonaDTO;
import sistemacine.service.dto.ReporteDTO;

/**
 * Mapper for the entity {@link Reporte} and its DTO {@link ReporteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReporteMapper extends EntityMapper<ReporteDTO, Reporte> {
    @Mapping(target = "vendedor", source = "vendedor", qualifiedByName = "personaNombre")
    ReporteDTO toDto(Reporte s);

    @Named("personaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    PersonaDTO toDtoPersonaNombre(Persona persona);
}
