package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Funcion.
 */
@Table("funcion")
public class Funcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private LocalDate fecha;

    @NotNull(message = "must not be null")
    @Column("hora_inicio")
    private Instant horaInicio;

    @NotNull(message = "must not be null")
    @Column("hora_fin")
    private Instant horaFin;

    @NotNull(message = "must not be null")
    @Column("precio")
    private BigDecimal precio;

    @Transient
    private Sala sala;

    @Transient
    @JsonIgnoreProperties(value = { "genero", "promociones" }, allowSetters = true)
    private Pelicula pelicula;

    @Transient
    private Tarifa tarifa;

    @Column("sala_id")
    private Long salaId;

    @Column("pelicula_id")
    private Long peliculaId;

    @Column("tarifa_id")
    private Long tarifaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Funcion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Funcion fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Instant getHoraInicio() {
        return this.horaInicio;
    }

    public Funcion horaInicio(Instant horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(Instant horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Instant getHoraFin() {
        return this.horaFin;
    }

    public Funcion horaFin(Instant horaFin) {
        this.setHoraFin(horaFin);
        return this;
    }

    public void setHoraFin(Instant horaFin) {
        this.horaFin = horaFin;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Funcion precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio != null ? precio.stripTrailingZeros() : null;
    }

    public Sala getSala() {
        return this.sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
        this.salaId = sala != null ? sala.getId() : null;
    }

    public Funcion sala(Sala sala) {
        this.setSala(sala);
        return this;
    }

    public Pelicula getPelicula() {
        return this.pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        this.peliculaId = pelicula != null ? pelicula.getId() : null;
    }

    public Funcion pelicula(Pelicula pelicula) {
        this.setPelicula(pelicula);
        return this;
    }

    public Tarifa getTarifa() {
        return this.tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
        this.tarifaId = tarifa != null ? tarifa.getId() : null;
    }

    public Funcion tarifa(Tarifa tarifa) {
        this.setTarifa(tarifa);
        return this;
    }

    public Long getSalaId() {
        return this.salaId;
    }

    public void setSalaId(Long sala) {
        this.salaId = sala;
    }

    public Long getPeliculaId() {
        return this.peliculaId;
    }

    public void setPeliculaId(Long pelicula) {
        this.peliculaId = pelicula;
    }

    public Long getTarifaId() {
        return this.tarifaId;
    }

    public void setTarifaId(Long tarifa) {
        this.tarifaId = tarifa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcion)) {
            return false;
        }
        return id != null && id.equals(((Funcion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Funcion{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
