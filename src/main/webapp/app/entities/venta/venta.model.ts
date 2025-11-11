import dayjs from 'dayjs/esm';
import { IDetalleVenta } from 'app/entities/detalle-venta/detalle-venta.model';
import { IPersona } from 'app/entities/persona/persona.model';

export interface IVenta {
  id?: string;
  fecha?: dayjs.Dayjs;
  total?: number;
  metodoPago?: string;
  detalles?: IDetalleVenta[] | null;
  cliente?: IPersona | null;
  vendedor?: IPersona | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: string,
    public fecha?: dayjs.Dayjs,
    public total?: number,
    public metodoPago?: string,
    public detalles?: IDetalleVenta[] | null,
    public cliente?: IPersona | null,
    public vendedor?: IPersona | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): string | undefined {
  return venta.id;
}
