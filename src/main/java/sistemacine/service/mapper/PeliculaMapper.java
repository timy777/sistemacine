package sistemacine.service.mapper;

import org.mapstruct.*;
import sistemacine.domain.Genero;
import sistemacine.domain.Pelicula;
import sistemacine.service.dto.GeneroDTO;
import sistemacine.service.dto.PeliculaDTO;

/**
 * Mapper for the entity {@link Pelicula} and its DTO {@link PeliculaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeliculaMapper extends EntityMapper<PeliculaDTO, Pelicula> {
    @Mapping(target = "genero", source = "genero", qualifiedByName = "generoNombre")
    PeliculaDTO toDto(Pelicula s);

    @Named("generoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    GeneroDTO toDtoGeneroNombre(Genero genero);
}
