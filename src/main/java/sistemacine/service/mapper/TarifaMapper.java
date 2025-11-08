package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Tarifa;
import sistemacine.service.dto.TarifaDTO;

/**
 * Mapper for the entity {@link Tarifa} and its DTO {@link TarifaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TarifaMapper extends EntityMapper<TarifaDTO, Tarifa> {}
