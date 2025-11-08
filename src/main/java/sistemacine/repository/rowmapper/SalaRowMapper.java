package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Sala;
import sistemacine.domain.enumeration.TipoSala;

/**
 * Converter between {@link Row} to {@link Sala}, with proper type conversions.
 */
@Service
public class SalaRowMapper implements BiFunction<Row, String, Sala> {

    private final ColumnConverter converter;

    public SalaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sala} stored in the database.
     */
    @Override
    public Sala apply(Row row, String prefix) {
        Sala entity = new Sala();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setCapacidad(converter.fromRow(row, prefix + "_capacidad", Integer.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", TipoSala.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", String.class));
        return entity;
    }
}
