package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FuncionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("hora_inicio", table, columnPrefix + "_hora_inicio"));
        columns.add(Column.aliased("hora_fin", table, columnPrefix + "_hora_fin"));
        columns.add(Column.aliased("precio", table, columnPrefix + "_precio"));

        columns.add(Column.aliased("sala_id", table, columnPrefix + "_sala_id"));
        columns.add(Column.aliased("pelicula_id", table, columnPrefix + "_pelicula_id"));
        columns.add(Column.aliased("tarifa_id", table, columnPrefix + "_tarifa_id"));
        return columns;
    }
}
