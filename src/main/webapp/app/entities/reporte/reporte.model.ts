import dayjs from 'dayjs/esm';
import { IPersona } from 'app/entities/persona/persona.model';

export interface IReporte {
  id?: number;
  tipo?: string;
  fechaGeneracion?: dayjs.Dayjs;
  descripcion?: string | null;
  vendedor?: IPersona | null;
}

export class Reporte implements IReporte {
  constructor(
    public id?: number,
    public tipo?: string,
    public fechaGeneracion?: dayjs.Dayjs,
    public descripcion?: string | null,
    public vendedor?: IPersona | null
  ) {}
}

export function getReporteIdentifier(reporte: IReporte): number | undefined {
  return reporte.id;
}
