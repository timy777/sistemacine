package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Tarifa;
import sistemacine.domain.enumeration.TipoSala;

/**
 * Converter between {@link Row} to {@link Tarifa}, with proper type conversions.
 */
@Service
public class TarifaRowMapper implements BiFunction<Row, String, Tarifa> {

    private final ColumnConverter converter;

    public TarifaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Tarifa} stored in the database.
     */
    @Override
    public Tarifa apply(Row row, String prefix) {
        Tarifa entity = new Tarifa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setMonto(converter.fromRow(row, prefix + "_monto", BigDecimal.class));
        entity.setDiaSemana(converter.fromRow(row, prefix + "_dia_semana", String.class));
        entity.setTipoSala(converter.fromRow(row, prefix + "_tipo_sala", TipoSala.class));
        return entity;
    }
}
