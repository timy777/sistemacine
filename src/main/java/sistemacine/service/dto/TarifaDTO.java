package sistemacine.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;
import sistemacine.domain.enumeration.TipoSala;

/**
 * A DTO for the {@link sistemacine.domain.Tarifa} entity.
 */
public class TarifaDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String nombre;

    private String descripcion;

    @NotNull(message = "must not be null")
    private BigDecimal monto;

    private String diaSemana;

    private TipoSala tipoSala;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public TipoSala getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifaDTO)) {
            return false;
        }

        TarifaDTO tarifaDTO = (TarifaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarifaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifaDTO{" +
            "id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", monto=" + getMonto() +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", tipoSala='" + getTipoSala() + "'" +
            "}";
    }
}
