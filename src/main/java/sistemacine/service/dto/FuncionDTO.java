package sistemacine.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sistemacine.domain.Funcion} entity.
 */
public class FuncionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate fecha;

    @NotNull(message = "must not be null")
    private Instant horaInicio;

    @NotNull(message = "must not be null")
    private Instant horaFin;

    @NotNull(message = "must not be null")
    private BigDecimal precio;

    private SalaDTO sala;

    private PeliculaDTO pelicula;

    private TarifaDTO tarifa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Instant getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Instant horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Instant getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Instant horaFin) {
        this.horaFin = horaFin;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public SalaDTO getSala() {
        return sala;
    }

    public void setSala(SalaDTO sala) {
        this.sala = sala;
    }

    public PeliculaDTO getPelicula() {
        return pelicula;
    }

    public void setPelicula(PeliculaDTO pelicula) {
        this.pelicula = pelicula;
    }

    public TarifaDTO getTarifa() {
        return tarifa;
    }

    public void setTarifa(TarifaDTO tarifa) {
        this.tarifa = tarifa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FuncionDTO)) {
            return false;
        }

        FuncionDTO funcionDTO = (FuncionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, funcionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncionDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            ", precio=" + getPrecio() +
            ", sala=" + getSala() +
            ", pelicula=" + getPelicula() +
            ", tarifa=" + getTarifa() +
            "}";
    }
}
