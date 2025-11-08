package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Venta.
 */
@Table("venta")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private Instant fecha;

    @NotNull(message = "must not be null")
    @Column("total")
    private BigDecimal total;

    @NotNull(message = "must not be null")
    @Column("metodo_pago")
    private String metodoPago;

    @Transient
    @JsonIgnoreProperties(value = { "funcion", "venta" }, allowSetters = true)
    private Set<DetalleVenta> detalles = new HashSet<>();

    @Transient
    private Persona cliente;

    @Transient
    private Persona vendedor;

    @Column("cliente_id")
    private Long clienteId;

    @Column("vendedor_id")
    private Long vendedorId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Venta total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total != null ? total.stripTrailingZeros() : null;
    }

    public String getMetodoPago() {
        return this.metodoPago;
    }

    public Venta metodoPago(String metodoPago) {
        this.setMetodoPago(metodoPago);
        return this;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Set<DetalleVenta> getDetalles() {
        return this.detalles;
    }

    public void setDetalles(Set<DetalleVenta> detalleVentas) {
        if (this.detalles != null) {
            this.detalles.forEach(i -> i.setVenta(null));
        }
        if (detalleVentas != null) {
            detalleVentas.forEach(i -> i.setVenta(this));
        }
        this.detalles = detalleVentas;
    }

    public Venta detalles(Set<DetalleVenta> detalleVentas) {
        this.setDetalles(detalleVentas);
        return this;
    }

    public Venta addDetalles(DetalleVenta detalleVenta) {
        this.detalles.add(detalleVenta);
        detalleVenta.setVenta(this);
        return this;
    }

    public Venta removeDetalles(DetalleVenta detalleVenta) {
        this.detalles.remove(detalleVenta);
        detalleVenta.setVenta(null);
        return this;
    }

    public Persona getCliente() {
        return this.cliente;
    }

    public void setCliente(Persona persona) {
        this.cliente = persona;
        this.clienteId = persona != null ? persona.getId() : null;
    }

    public Venta cliente(Persona persona) {
        this.setCliente(persona);
        return this;
    }

    public Persona getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(Persona persona) {
        this.vendedor = persona;
        this.vendedorId = persona != null ? persona.getId() : null;
    }

    public Venta vendedor(Persona persona) {
        this.setVendedor(persona);
        return this;
    }

    public Long getClienteId() {
        return this.clienteId;
    }

    public void setClienteId(Long persona) {
        this.clienteId = persona;
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
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", metodoPago='" + getMetodoPago() + "'" +
            "}";
    }
}
