import dayjs from 'dayjs/esm';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';

export interface IPromocion {
  id?: string;
  nombre?: string;
  descripcion?: string | null;
  porcentajeDescuento?: number;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  activa?: boolean;
  peliculas?: IPelicula[] | null;
}

export class Promocion implements IPromocion {
  constructor(
    public id?: string,
    public nombre?: string,
    public descripcion?: string | null,
    public porcentajeDescuento?: number,
    public fechaInicio?: dayjs.Dayjs,
    public fechaFin?: dayjs.Dayjs,
    public activa?: boolean,
    public peliculas?: IPelicula[] | null
  ) {
    this.activa = this.activa ?? false;
  }
}

export function getPromocionIdentifier(promocion: IPromocion): string | undefined {
  return promocion.id;
}
