package sistemacine.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sistemacine.domain.DetalleVenta} entity.
 */
public class DetalleVentaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String asiento;

    @NotNull(message = "must not be null")
    private BigDecimal precioUnitario;

    private FuncionDTO funcion;

    private VentaDTO venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public FuncionDTO getFuncion() {
        return funcion;
    }

    public void setFuncion(FuncionDTO funcion) {
        this.funcion = funcion;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVentaDTO)) {
            return false;
        }

        DetalleVentaDTO detalleVentaDTO = (DetalleVentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detalleVentaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVentaDTO{" +
            "id=" + getId() +
            ", asiento='" + getAsiento() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            ", funcion=" + getFuncion() +
            ", venta=" + getVenta() +
            "}";
    }
}
