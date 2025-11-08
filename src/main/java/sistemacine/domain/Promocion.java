package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Promocion.
 */
@Table("promocion")
public class Promocion implements Serializable {

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
    @Column("porcentaje_descuento")
    private Double porcentajeDescuento;

    @NotNull(message = "must not be null")
    @Column("fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "must not be null")
    @Column("fecha_fin")
    private LocalDate fechaFin;

    @NotNull(message = "must not be null")
    @Column("activa")
    private Boolean activa;

    @Transient
    @JsonIgnoreProperties(value = { "genero", "promociones" }, allowSetters = true)
    private Set<Pelicula> peliculas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Promocion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Promocion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Promocion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPorcentajeDescuento() {
        return this.porcentajeDescuento;
    }

    public Promocion porcentajeDescuento(Double porcentajeDescuento) {
        this.setPorcentajeDescuento(porcentajeDescuento);
        return this;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Promocion fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Promocion fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public Promocion activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Set<Pelicula> getPeliculas() {
        return this.peliculas;
    }

    public void setPeliculas(Set<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public Promocion peliculas(Set<Pelicula> peliculas) {
        this.setPeliculas(peliculas);
        return this;
    }

    public Promocion addPeliculas(Pelicula pelicula) {
        this.peliculas.add(pelicula);
        pelicula.getPromociones().add(this);
        return this;
    }

    public Promocion removePeliculas(Pelicula pelicula) {
        this.peliculas.remove(pelicula);
        pelicula.getPromociones().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promocion)) {
            return false;
        }
        return id != null && id.equals(((Promocion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promocion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", porcentajeDescuento=" + getPorcentajeDescuento() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", activa='" + getActiva() + "'" +
            "}";
    }
}
