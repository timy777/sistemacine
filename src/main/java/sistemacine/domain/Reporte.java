package sistemacine.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Reporte.
 */
@Document(collection = "reporte")
public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("tipo")
    private String tipo;

    @NotNull(message = "must not be null")
    @Field("fecha_generacion")
    private LocalDate fechaGeneracion;

    @Field("descripcion")
    private String descripcion;

    @Field("vendedor")
    private Persona vendedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Reporte id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Reporte tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaGeneracion() {
        return this.fechaGeneracion;
    }

    public Reporte fechaGeneracion(LocalDate fechaGeneracion) {
        this.setFechaGeneracion(fechaGeneracion);
        return this;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Reporte descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Persona getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(Persona persona) {
        this.vendedor = persona;
    }

    public Reporte vendedor(Persona persona) {
        this.setVendedor(persona);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reporte)) {
            return false;
        }
        return id != null && id.equals(((Reporte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reporte{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", fechaGeneracion='" + getFechaGeneracion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
