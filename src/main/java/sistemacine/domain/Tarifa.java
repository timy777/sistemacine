package sistemacine.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import sistemacine.domain.enumeration.TipoSala;

/**
 * A Tarifa.
 */
@Table("tarifa")
public class Tarifa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @Column("descripcion")
    private String descripcion;

    @NotNull(message = "must not be null")
    @Column("monto")
    private BigDecimal monto;

    @Column("dia_semana")
    private String diaSemana;

    @Column("tipo_sala")
    private TipoSala tipoSala;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarifa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Tarifa nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Tarifa descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public Tarifa monto(BigDecimal monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto != null ? monto.stripTrailingZeros() : null;
    }

    public String getDiaSemana() {
        return this.diaSemana;
    }

    public Tarifa diaSemana(String diaSemana) {
        this.setDiaSemana(diaSemana);
        return this;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public TipoSala getTipoSala() {
        return this.tipoSala;
    }

    public Tarifa tipoSala(TipoSala tipoSala) {
        this.setTipoSala(tipoSala);
        return this;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarifa)) {
            return false;
        }
        return id != null && id.equals(((Tarifa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarifa{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", monto=" + getMonto() +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", tipoSala='" + getTipoSala() + "'" +
            "}";
    }
}
