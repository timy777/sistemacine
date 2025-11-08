package sistemacine.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sistemacine.domain.Pelicula;
import sistemacine.domain.Promocion;
import sistemacine.service.dto.PeliculaDTO;
import sistemacine.service.dto.PromocionDTO;

/**
 * Mapper for the entity {@link Promocion} and its DTO {@link PromocionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PromocionMapper extends EntityMapper<PromocionDTO, Promocion> {
    @Mapping(target = "peliculas", source = "peliculas", qualifiedByName = "peliculaIdSet")
    PromocionDTO toDto(Promocion s);

    @Mapping(target = "removePeliculas", ignore = true)
    Promocion toEntity(PromocionDTO promocionDTO);

    @Named("peliculaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PeliculaDTO toDtoPeliculaId(Pelicula pelicula);

    @Named("peliculaIdSet")
    default Set<PeliculaDTO> toDtoPeliculaIdSet(Set<Pelicula> pelicula) {
        return pelicula.stream().map(this::toDtoPeliculaId).collect(Collectors.toSet());
    }
}
