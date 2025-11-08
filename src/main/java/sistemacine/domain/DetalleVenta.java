package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DetalleVenta.
 */
@Table("detalle_venta")
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("asiento")
    private String asiento;

    @NotNull(message = "must not be null")
    @Column("precio_unitario")
    private BigDecimal precioUnitario;

    @Transient
    @JsonIgnoreProperties(value = { "sala", "pelicula", "tarifa" }, allowSetters = true)
    private Funcion funcion;

    @Transient
    @JsonIgnoreProperties(value = { "detalles", "cliente", "vendedor" }, allowSetters = true)
    private Venta venta;

    @Column("funcion_id")
    private Long funcionId;

    @Column("venta_id")
    private Long ventaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsiento() {
        return this.asiento;
    }

    public DetalleVenta asiento(String asiento) {
        this.setAsiento(asiento);
        return this;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }

    public DetalleVenta precioUnitario(BigDecimal precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario != null ? precioUnitario.stripTrailingZeros() : null;
    }

    public Funcion getFuncion() {
        return this.funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
        this.funcionId = funcion != null ? funcion.getId() : null;
    }

    public DetalleVenta funcion(Funcion funcion) {
        this.setFuncion(funcion);
        return this;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
        this.ventaId = venta != null ? venta.getId() : null;
    }

    public DetalleVenta venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Long getFuncionId() {
        return this.funcionId;
    }

    public void setFuncionId(Long funcion) {
        this.funcionId = funcion;
    }

    public Long getVentaId() {
        return this.ventaId;
    }

    public void setVentaId(Long venta) {
        this.ventaId = venta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVenta)) {
            return false;
        }
        return id != null && id.equals(((DetalleVenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVenta{" +
            "id=" + getId() +
            ", asiento='" + getAsiento() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            "}";
    }
}
