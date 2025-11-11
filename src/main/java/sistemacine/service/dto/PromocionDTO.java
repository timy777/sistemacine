package sistemacine.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sistemacine.domain.Promocion} entity.
 */
public class PromocionDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String nombre;

    private String descripcion;

    @NotNull(message = "must not be null")
    private Double porcentajeDescuento;

    @NotNull(message = "must not be null")
    private LocalDate fechaInicio;

    @NotNull(message = "must not be null")
    private LocalDate fechaFin;

    @NotNull(message = "must not be null")
    private Boolean activa;

    private Set<PeliculaDTO> peliculas = new HashSet<>();

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

    public Double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Set<PeliculaDTO> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(Set<PeliculaDTO> peliculas) {
        this.peliculas = peliculas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromocionDTO)) {
            return false;
        }

        PromocionDTO promocionDTO = (PromocionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promocionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromocionDTO{" +
            "id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", porcentajeDescuento=" + getPorcentajeDescuento() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", activa='" + getActiva() + "'" +
            ", peliculas=" + getPeliculas() +
            "}";
    }
}
