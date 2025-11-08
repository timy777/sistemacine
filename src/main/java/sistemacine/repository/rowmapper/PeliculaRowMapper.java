package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Pelicula;
import sistemacine.domain.enumeration.EstadoPelicula;

/**
 * Converter between {@link Row} to {@link Pelicula}, with proper type conversions.
 */
@Service
public class PeliculaRowMapper implements BiFunction<Row, String, Pelicula> {

    private final ColumnConverter converter;

    public PeliculaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Pelicula} stored in the database.
     */
    @Override
    public Pelicula apply(Row row, String prefix) {
        Pelicula entity = new Pelicula();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitulo(converter.fromRow(row, prefix + "_titulo", String.class));
        entity.setSinopsis(converter.fromRow(row, prefix + "_sinopsis", String.class));
        entity.setDuracion(converter.fromRow(row, prefix + "_duracion", Integer.class));
        entity.setIdioma(converter.fromRow(row, prefix + "_idioma", String.class));
        entity.setClasificacion(converter.fromRow(row, prefix + "_clasificacion", String.class));
        entity.setFormato(converter.fromRow(row, prefix + "_formato", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", EstadoPelicula.class));
        entity.setImagenUrl(converter.fromRow(row, prefix + "_imagen_url", String.class));
        entity.setGeneroId(converter.fromRow(row, prefix + "_genero_id", Long.class));
        return entity;
    }
}
