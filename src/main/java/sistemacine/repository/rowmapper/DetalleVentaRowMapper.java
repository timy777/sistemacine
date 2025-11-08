package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.DetalleVenta;

/**
 * Converter between {@link Row} to {@link DetalleVenta}, with proper type conversions.
 */
@Service
public class DetalleVentaRowMapper implements BiFunction<Row, String, DetalleVenta> {

    private final ColumnConverter converter;

    public DetalleVentaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DetalleVenta} stored in the database.
     */
    @Override
    public DetalleVenta apply(Row row, String prefix) {
        DetalleVenta entity = new DetalleVenta();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAsiento(converter.fromRow(row, prefix + "_asiento", String.class));
        entity.setPrecioUnitario(converter.fromRow(row, prefix + "_precio_unitario", BigDecimal.class));
        entity.setFuncionId(converter.fromRow(row, prefix + "_funcion_id", Long.class));
        entity.setVentaId(converter.fromRow(row, prefix + "_venta_id", Long.class));
        return entity;
    }
}
