package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VentaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("total", table, columnPrefix + "_total"));
        columns.add(Column.aliased("metodo_pago", table, columnPrefix + "_metodo_pago"));

        columns.add(Column.aliased("cliente_id", table, columnPrefix + "_cliente_id"));
        columns.add(Column.aliased("vendedor_id", table, columnPrefix + "_vendedor_id"));
        return columns;
    }
}
