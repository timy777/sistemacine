package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Genero;

/**
 * Converter between {@link Row} to {@link Genero}, with proper type conversions.
 */
@Service
public class GeneroRowMapper implements BiFunction<Row, String, Genero> {

    private final ColumnConverter converter;

    public GeneroRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Genero} stored in the database.
     */
    @Override
    public Genero apply(Row row, String prefix) {
        Genero entity = new Genero();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        return entity;
    }
}
