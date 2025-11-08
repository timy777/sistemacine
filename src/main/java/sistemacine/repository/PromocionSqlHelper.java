package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PromocionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));
        columns.add(Column.aliased("porcentaje_descuento", table, columnPrefix + "_porcentaje_descuento"));
        columns.add(Column.aliased("fecha_inicio", table, columnPrefix + "_fecha_inicio"));
        columns.add(Column.aliased("fecha_fin", table, columnPrefix + "_fecha_fin"));
        columns.add(Column.aliased("activa", table, columnPrefix + "_activa"));

        return columns;
    }
}
