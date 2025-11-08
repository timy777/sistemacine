package sistemacine.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Reporte.
 */
@Table("reporte")
public class Reporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("tipo")
    private String tipo;

    @NotNull(message = "must not be null")
    @Column("fecha_generacion")
    private LocalDate fechaGeneracion;

    @Column("descripcion")
    private String descripcion;

    @Transient
    private Persona vendedor;

    @Column("vendedor_id")
    private Long vendedorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reporte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
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
        this.vendedorId = persona != null ? persona.getId() : null;
    }

    public Reporte vendedor(Persona persona) {
        this.setVendedor(persona);
        return this;
    }

    public Long getVendedorId() {
        return this.vendedorId;
    }

    public void setVendedorId(Long persona) {
        this.vendedorId = persona;
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
