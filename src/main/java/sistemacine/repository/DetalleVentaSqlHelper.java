package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DetalleVentaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("asiento", table, columnPrefix + "_asiento"));
        columns.add(Column.aliased("precio_unitario", table, columnPrefix + "_precio_unitario"));

        columns.add(Column.aliased("funcion_id", table, columnPrefix + "_funcion_id"));
        columns.add(Column.aliased("venta_id", table, columnPrefix + "_venta_id"));
        return columns;
    }
}
