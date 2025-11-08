import dayjs from 'dayjs/esm';
import { ISala } from 'app/entities/sala/sala.model';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';

export interface IFuncion {
  id?: number;
  fecha?: dayjs.Dayjs;
  horaInicio?: dayjs.Dayjs;
  horaFin?: dayjs.Dayjs;
  precio?: number;
  sala?: ISala | null;
  pelicula?: IPelicula | null;
  tarifa?: ITarifa | null;
}

export class Funcion implements IFuncion {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public horaInicio?: dayjs.Dayjs,
    public horaFin?: dayjs.Dayjs,
    public precio?: number,
    public sala?: ISala | null,
    public pelicula?: IPelicula | null,
    public tarifa?: ITarifa | null
  ) {}
}

export function getFuncionIdentifier(funcion: IFuncion): number | undefined {
  return funcion.id;
}
