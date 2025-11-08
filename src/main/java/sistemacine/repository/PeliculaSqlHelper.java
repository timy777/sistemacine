package sistemacine.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PeliculaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("titulo", table, columnPrefix + "_titulo"));
        columns.add(Column.aliased("sinopsis", table, columnPrefix + "_sinopsis"));
        columns.add(Column.aliased("duracion", table, columnPrefix + "_duracion"));
        columns.add(Column.aliased("idioma", table, columnPrefix + "_idioma"));
        columns.add(Column.aliased("clasificacion", table, columnPrefix + "_clasificacion"));
        columns.add(Column.aliased("formato", table, columnPrefix + "_formato"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));
        columns.add(Column.aliased("imagen_url", table, columnPrefix + "_imagen_url"));

        columns.add(Column.aliased("genero_id", table, columnPrefix + "_genero_id"));
        return columns;
    }
}
