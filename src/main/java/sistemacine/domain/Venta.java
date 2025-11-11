package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Venta.
 */
@Document(collection = "venta")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("fecha")
    private Instant fecha;

    @NotNull(message = "must not be null")
    @Field("total")
    private BigDecimal total;

    @NotNull(message = "must not be null")
    @Field("metodo_pago")
    private String metodoPago;

    @Field("detalles")
    @JsonIgnoreProperties(value = { "funcion", "venta" }, allowSetters = true)
    private Set<DetalleVenta> detalles = new HashSet<>();

    @Field("cliente")
    private Persona cliente;

    @Field("vendedor")
    private Persona vendedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Venta id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
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
        this.total = total;
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
    }

    public Venta vendedor(Persona persona) {
        this.setVendedor(persona);
        return this;
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
