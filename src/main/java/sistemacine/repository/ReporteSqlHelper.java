package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ReporteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("tipo", table, columnPrefix + "_tipo"));
        columns.add(Column.aliased("fecha_generacion", table, columnPrefix + "_fecha_generacion"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));

        columns.add(Column.aliased("vendedor_id", table, columnPrefix + "_vendedor_id"));
        return columns;
    }
}
