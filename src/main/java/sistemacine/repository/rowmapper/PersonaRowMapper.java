package sistemacine.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sistemacine.domain.Persona;
import sistemacine.domain.enumeration.Sexo;
import sistemacine.domain.enumeration.TipoPersona;

/**
 * Converter between {@link Row} to {@link Persona}, with proper type conversions.
 */
@Service
public class PersonaRowMapper implements BiFunction<Row, String, Persona> {

    private final ColumnConverter converter;

    public PersonaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Persona} stored in the database.
     */
    @Override
    public Persona apply(Row row, String prefix) {
        Persona entity = new Persona();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setApellido(converter.fromRow(row, prefix + "_apellido", String.class));
        entity.setTelefono(converter.fromRow(row, prefix + "_telefono", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", TipoPersona.class));
        entity.setFechaNacimiento(converter.fromRow(row, prefix + "_fecha_nacimiento", LocalDate.class));
        entity.setSexo(converter.fromRow(row, prefix + "_sexo", Sexo.class));
        entity.setCarnetIdentidad(converter.fromRow(row, prefix + "_carnet_identidad", String.class));
        return entity;
    }
}
