package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Persona;
import sistemacine.domain.Venta;
import sistemacine.service.dto.PersonaDTO;
import sistemacine.service.dto.VentaDTO;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "personaNombre")
    @Mapping(target = "vendedor", source = "vendedor", qualifiedByName = "personaNombre")
    VentaDTO toDto(Venta s);

    @Named("personaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    PersonaDTO toDtoPersonaNombre(Persona persona);
}
