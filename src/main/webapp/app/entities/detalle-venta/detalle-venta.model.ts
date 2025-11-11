import { IFuncion } from 'app/entities/funcion/funcion.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface IDetalleVenta {
  id?: string;
  asiento?: string;
  precioUnitario?: number;
  funcion?: IFuncion | null;
  venta?: IVenta | null;
}

export class DetalleVenta implements IDetalleVenta {
  constructor(
    public id?: string,
    public asiento?: string,
    public precioUnitario?: number,
    public funcion?: IFuncion | null,
    public venta?: IVenta | null
  ) {}
}

export function getDetalleVentaIdentifier(detalleVenta: IDetalleVenta): string | undefined {
  return detalleVenta.id;
}
