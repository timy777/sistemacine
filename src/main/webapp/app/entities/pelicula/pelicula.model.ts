import { IGenero } from 'app/entities/genero/genero.model';
import { IPromocion } from 'app/entities/promocion/promocion.model';
import { EstadoPelicula } from 'app/entities/enumerations/estado-pelicula.model';

export interface IPelicula {
  id?: number;
  titulo?: string;
  sinopsis?: string | null;
  duracion?: number;
  idioma?: string | null;
  clasificacion?: string | null;
  formato?: string | null;
  estado?: EstadoPelicula;
  imagenUrl?: string | null;
  genero?: IGenero | null;
  promociones?: IPromocion[] | null;
}

export class Pelicula implements IPelicula {
  constructor(
    public id?: number,
    public titulo?: string,
    public sinopsis?: string | null,
    public duracion?: number,
    public idioma?: string | null,
    public clasificacion?: string | null,
    public formato?: string | null,
    public estado?: EstadoPelicula,
    public imagenUrl?: string | null,
    public genero?: IGenero | null,
    public promociones?: IPromocion[] | null
  ) {}
}

export function getPeliculaIdentifier(pelicula: IPelicula): number | undefined {
  return pelicula.id;
}
