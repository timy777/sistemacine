package sistemacine.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import sistemacine.domain.enumeration.EstadoPelicula;

/**
 * A DTO for the {@link sistemacine.domain.Pelicula} entity.
 */
public class PeliculaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String titulo;

    @Lob
    private String sinopsis;

    @NotNull(message = "must not be null")
    private Integer duracion;

    private String idioma;

    private String clasificacion;

    private String formato;

    @NotNull(message = "must not be null")
    private EstadoPelicula estado;

    private String imagenUrl;

    private GeneroDTO genero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public EstadoPelicula getEstado() {
        return estado;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public GeneroDTO getGenero() {
        return genero;
    }

    public void setGenero(GeneroDTO genero) {
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeliculaDTO)) {
            return false;
        }

        PeliculaDTO peliculaDTO = (PeliculaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peliculaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeliculaDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", sinopsis='" + getSinopsis() + "'" +
            ", duracion=" + getDuracion() +
            ", idioma='" + getIdioma() + "'" +
            ", clasificacion='" + getClasificacion() + "'" +
            ", formato='" + getFormato() + "'" +
            ", estado='" + getEstado() + "'" +
            ", imagenUrl='" + getImagenUrl() + "'" +
            ", genero=" + getGenero() +
            "}";
    }
}
