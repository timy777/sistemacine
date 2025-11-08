package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.DetalleVenta;
import sistemacine.domain.Funcion;
import sistemacine.domain.Venta;
import sistemacine.service.dto.DetalleVentaDTO;
import sistemacine.service.dto.FuncionDTO;
import sistemacine.service.dto.VentaDTO;

/**
 * Mapper for the entity {@link DetalleVenta} and its DTO {@link DetalleVentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetalleVentaMapper extends EntityMapper<DetalleVentaDTO, DetalleVenta> {
    @Mapping(target = "funcion", source = "funcion", qualifiedByName = "funcionId")
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    DetalleVentaDTO toDto(DetalleVenta s);

    @Named("funcionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FuncionDTO toDtoFuncionId(Funcion funcion);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
