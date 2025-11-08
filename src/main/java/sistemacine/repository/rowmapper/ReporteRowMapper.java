package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Reporte;

/**
 * Converter between {@link Row} to {@link Reporte}, with proper type conversions.
 */
@Service
public class ReporteRowMapper implements BiFunction<Row, String, Reporte> {

    private final ColumnConverter converter;

    public ReporteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Reporte} stored in the database.
     */
    @Override
    public Reporte apply(Row row, String prefix) {
        Reporte entity = new Reporte();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", String.class));
        entity.setFechaGeneracion(converter.fromRow(row, prefix + "_fecha_generacion", LocalDate.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setVendedorId(converter.fromRow(row, prefix + "_vendedor_id", Long.class));
        return entity;
    }
}
