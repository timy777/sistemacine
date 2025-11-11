package sistemacine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import sistemacine.domain.enumeration.EstadoPelicula;

/**
 * A Pelicula.
 */
@Document(collection = "pelicula")
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("titulo")
    private String titulo;

    @Field("sinopsis")
    private String sinopsis;

    @NotNull(message = "must not be null")
    @Field("duracion")
    private Integer duracion;

    @Field("idioma")
    private String idioma;

    @Field("clasificacion")
    private String clasificacion;

    @Field("formato")
    private String formato;

    @NotNull(message = "must not be null")
    @Field("estado")
    private EstadoPelicula estado;

    @Field("imagen_url")
    private String imagenUrl;

    @Field("genero")
    private Genero genero;

    @Field("promociones")
    @JsonIgnoreProperties(value = { "peliculas" }, allowSetters = true)
    private Set<Promocion> promociones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Pelicula id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Pelicula titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return this.sinopsis;
    }

    public Pelicula sinopsis(String sinopsis) {
        this.setSinopsis(sinopsis);
        return this;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Integer getDuracion() {
        return this.duracion;
    }

    public Pelicula duracion(Integer duracion) {
        this.setDuracion(duracion);
        return this;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getIdioma() {
        return this.idioma;
    }

    public Pelicula idioma(String idioma) {
        this.setIdioma(idioma);
        return this;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getClasificacion() {
        return this.clasificacion;
    }

    public Pelicula clasificacion(String clasificacion) {
        this.setClasificacion(clasificacion);
        return this;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getFormato() {
        return this.formato;
    }

    public Pelicula formato(String formato) {
        this.setFormato(formato);
        return this;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public EstadoPelicula getEstado() {
        return this.estado;
    }

    public Pelicula estado(EstadoPelicula estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public Pelicula imagenUrl(String imagenUrl) {
        this.setImagenUrl(imagenUrl);
        return this;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Pelicula genero(Genero genero) {
        this.setGenero(genero);
        return this;
    }

    public Set<Promocion> getPromociones() {
        return this.promociones;
    }

    public void setPromociones(Set<Promocion> promocions) {
        if (this.promociones != null) {
            this.promociones.forEach(i -> i.removePeliculas(this));
        }
        if (promocions != null) {
            promocions.forEach(i -> i.addPeliculas(this));
        }
        this.promociones = promocions;
    }

    public Pelicula promociones(Set<Promocion> promocions) {
        this.setPromociones(promocions);
        return this;
    }

    public Pelicula addPromociones(Promocion promocion) {
        this.promociones.add(promocion);
        promocion.getPeliculas().add(this);
        return this;
    }

    public Pelicula removePromociones(Promocion promocion) {
        this.promociones.remove(promocion);
        promocion.getPeliculas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pelicula)) {
            return false;
        }
        return id != null && id.equals(((Pelicula) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", sinopsis='" + getSinopsis() + "'" +
            ", duracion=" + getDuracion() +
            ", idioma='" + getIdioma() + "'" +
            ", clasificacion='" + getClasificacion() + "'" +
            ", formato='" + getFormato() + "'" +
            ", estado='" + getEstado() + "'" +
            ", imagenUrl='" + getImagenUrl() + "'" +
            "}";
    }
}
