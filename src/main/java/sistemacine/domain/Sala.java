package sistemacine.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import sistemacine.domain.enumeration.TipoSala;

/**
 * A Sala.
 */
@Document(collection = "sala")
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Field("capacidad")
    private Integer capacidad;

    @NotNull(message = "must not be null")
    @Field("tipo")
    private TipoSala tipo;

    @Field("estado")
    private String estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Sala id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Sala nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return this.capacidad;
    }

    public Sala capacidad(Integer capacidad) {
        this.setCapacidad(capacidad);
        return this;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public TipoSala getTipo() {
        return this.tipo;
    }

    public Sala tipo(TipoSala tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return this.estado;
    }

    public Sala estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sala)) {
            return false;
        }
        return id != null && id.equals(((Sala) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sala{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", capacidad=" + getCapacidad() +
            ", tipo='" + getTipo() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
