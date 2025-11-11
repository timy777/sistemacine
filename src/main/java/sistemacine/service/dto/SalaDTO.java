package sistemacine.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import sistemacine.domain.enumeration.TipoSala;

/**
 * A DTO for the {@link sistemacine.domain.Sala} entity.
 */
public class SalaDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String nombre;

    @NotNull(message = "must not be null")
    private Integer capacidad;

    @NotNull(message = "must not be null")
    private TipoSala tipo;

    private String estado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public TipoSala getTipo() {
        return tipo;
    }

    public void setTipo(TipoSala tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalaDTO)) {
            return false;
        }

        SalaDTO salaDTO = (SalaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaDTO{" +
            "id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", capacidad=" + getCapacidad() +
            ", tipo='" + getTipo() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
