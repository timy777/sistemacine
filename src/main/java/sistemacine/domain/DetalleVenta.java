package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DetalleVenta.
 */
@Document(collection = "detalle_venta")
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("asiento")
    private String asiento;

    @NotNull(message = "must not be null")
    @Field("precio_unitario")
    private BigDecimal precioUnitario;

    @Field("funcion")
    @JsonIgnoreProperties(value = { "sala", "pelicula", "tarifa" }, allowSetters = true)
    private Funcion funcion;

    @Field("venta")
    @JsonIgnoreProperties(value = { "detalles", "cliente", "vendedor" }, allowSetters = true)
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public DetalleVenta id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
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
        this.precioUnitario = precioUnitario;
    }

    public Funcion getFuncion() {
        return this.funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
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
    }

    public DetalleVenta venta(Venta venta) {
        this.setVenta(venta);
        return this;
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
