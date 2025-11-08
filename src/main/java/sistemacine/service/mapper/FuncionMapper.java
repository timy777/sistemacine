package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Funcion;
import sistemacine.domain.Pelicula;
import sistemacine.domain.Sala;
import sistemacine.domain.Tarifa;
import sistemacine.service.dto.FuncionDTO;
import sistemacine.service.dto.PeliculaDTO;
import sistemacine.service.dto.SalaDTO;
import sistemacine.service.dto.TarifaDTO;

/**
 * Mapper for the entity {@link Funcion} and its DTO {@link FuncionDTO}.
 */
@Mapper(componentModel = "spring")
public interface FuncionMapper extends EntityMapper<FuncionDTO, Funcion> {
    @Mapping(target = "sala", source = "sala", qualifiedByName = "salaNombre")
    @Mapping(target = "pelicula", source = "pelicula", qualifiedByName = "peliculaTitulo")
    @Mapping(target = "tarifa", source = "tarifa", qualifiedByName = "tarifaNombre")
    FuncionDTO toDto(Funcion s);

    @Named("salaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    SalaDTO toDtoSalaNombre(Sala sala);

    @Named("peliculaTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    PeliculaDTO toDtoPeliculaTitulo(Pelicula pelicula);

    @Named("tarifaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TarifaDTO toDtoTarifaNombre(Tarifa tarifa);
}
