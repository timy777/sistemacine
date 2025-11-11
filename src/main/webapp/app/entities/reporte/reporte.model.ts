import dayjs from 'dayjs/esm';
import { IPersona } from 'app/entities/persona/persona.model';

export interface IReporte {
  id?: string;
  tipo?: string;
  fechaGeneracion?: dayjs.Dayjs;
  descripcion?: string | null;
  vendedor?: IPersona | null;
}

export class Reporte implements IReporte {
  constructor(
    public id?: string,
    public tipo?: string,
    public fechaGeneracion?: dayjs.Dayjs,
    public descripcion?: string | null,
    public vendedor?: IPersona | null
  ) {}
}

export function getReporteIdentifier(reporte: IReporte): string | undefined {
  return reporte.id;
}
