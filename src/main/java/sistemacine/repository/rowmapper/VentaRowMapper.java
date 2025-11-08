package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Venta;

/**
 * Converter between {@link Row} to {@link Venta}, with proper type conversions.
 */
@Service
public class VentaRowMapper implements BiFunction<Row, String, Venta> {

    private final ColumnConverter converter;

    public VentaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Venta} stored in the database.
     */
    @Override
    public Venta apply(Row row, String prefix) {
        Venta entity = new Venta();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", Instant.class));
        entity.setTotal(converter.fromRow(row, prefix + "_total", BigDecimal.class));
        entity.setMetodoPago(converter.fromRow(row, prefix + "_metodo_pago", String.class));
        entity.setClienteId(converter.fromRow(row, prefix + "_cliente_id", Long.class));
        entity.setVendedorId(converter.fromRow(row, prefix + "_vendedor_id", Long.class));
        return entity;
    }
}
