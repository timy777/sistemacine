import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';

export interface ITarifa {
  id?: string;
  nombre?: string;
  descripcion?: string | null;
  monto?: number;
  diaSemana?: string | null;
  tipoSala?: TipoSala | null;
}

export class Tarifa implements ITarifa {
  constructor(
    public id?: string,
    public nombre?: string,
    public descripcion?: string | null,
    public monto?: number,
    public diaSemana?: string | null,
    public tipoSala?: TipoSala | null
  ) {}
}

export function getTarifaIdentifier(tarifa: ITarifa): string | undefined {
  return tarifa.id;
}
