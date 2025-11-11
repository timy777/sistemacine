package sistemacine.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sistemacine.domain.Reporte} entity.
 */
public class ReporteDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String tipo;

    @NotNull(message = "must not be null")
    private LocalDate fechaGeneracion;

    private String descripcion;

    private PersonaDTO vendedor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public PersonaDTO getVendedor() {
        return vendedor;
    }

    public void setVendedor(PersonaDTO vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReporteDTO)) {
            return false;
        }

        ReporteDTO reporteDTO = (ReporteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reporteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReporteDTO{" +
            "id='" + getId() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fechaGeneracion='" + getFechaGeneracion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", vendedor=" + getVendedor() +
            "}";
    }
}
