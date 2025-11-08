package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TarifaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("descripcion", table, columnPrefix + "_descripcion"));
        columns.add(Column.aliased("monto", table, columnPrefix + "_monto"));
        columns.add(Column.aliased("dia_semana", table, columnPrefix + "_dia_semana"));
        columns.add(Column.aliased("tipo_sala", table, columnPrefix + "_tipo_sala"));

        return columns;
    }
}
