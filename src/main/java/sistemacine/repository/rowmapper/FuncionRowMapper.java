package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Funcion;

/**
 * Converter between {@link Row} to {@link Funcion}, with proper type conversions.
 */
@Service
public class FuncionRowMapper implements BiFunction<Row, String, Funcion> {

    private final ColumnConverter converter;

    public FuncionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Funcion} stored in the database.
     */
    @Override
    public Funcion apply(Row row, String prefix) {
        Funcion entity = new Funcion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", LocalDate.class));
        entity.setHoraInicio(converter.fromRow(row, prefix + "_hora_inicio", Instant.class));
        entity.setHoraFin(converter.fromRow(row, prefix + "_hora_fin", Instant.class));
        entity.setPrecio(converter.fromRow(row, prefix + "_precio", BigDecimal.class));
        entity.setSalaId(converter.fromRow(row, prefix + "_sala_id", Long.class));
        entity.setPeliculaId(converter.fromRow(row, prefix + "_pelicula_id", Long.class));
        entity.setTarifaId(converter.fromRow(row, prefix + "_tarifa_id", Long.class));
        return entity;
    }
}
