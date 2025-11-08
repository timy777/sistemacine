package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Promocion;

/**
 * Converter between {@link Row} to {@link Promocion}, with proper type conversions.
 */
@Service
public class PromocionRowMapper implements BiFunction<Row, String, Promocion> {

    private final ColumnConverter converter;

    public PromocionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Promocion} stored in the database.
     */
    @Override
    public Promocion apply(Row row, String prefix) {
        Promocion entity = new Promocion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        entity.setPorcentajeDescuento(converter.fromRow(row, prefix + "_porcentaje_descuento", Double.class));
        entity.setFechaInicio(converter.fromRow(row, prefix + "_fecha_inicio", LocalDate.class));
        entity.setFechaFin(converter.fromRow(row, prefix + "_fecha_fin", LocalDate.class));
        entity.setActiva(converter.fromRow(row, prefix + "_activa", Boolean.class));
        return entity;
    }
}
