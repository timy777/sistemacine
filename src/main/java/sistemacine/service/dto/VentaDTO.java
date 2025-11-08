package sistemacine.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sistemacine.domain.Venta} entity.
 */
public class VentaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant fecha;

    @NotNull(message = "must not be null")
    private BigDecimal total;

    @NotNull(message = "must not be null")
    private String metodoPago;

    private PersonaDTO cliente;

    private PersonaDTO vendedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public PersonaDTO getCliente() {
        return cliente;
    }

    public void setCliente(PersonaDTO cliente) {
        this.cliente = cliente;
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
        if (!(o instanceof VentaDTO)) {
            return false;
        }

        VentaDTO ventaDTO = (VentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", metodoPago='" + getMetodoPago() + "'" +
            ", cliente=" + getCliente() +
            ", vendedor=" + getVendedor() +
            "}";
    }
}
