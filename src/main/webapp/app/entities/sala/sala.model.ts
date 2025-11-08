import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';

export interface ISala {
  id?: number;
  nombre?: string;
  capacidad?: number;
  tipo?: TipoSala;
  estado?: string | null;
}

export class Sala implements ISala {
  constructor(
    public id?: number,
    public nombre?: string,
    public capacidad?: number,
    public tipo?: TipoSala,
    public estado?: string | null
  ) {}
}

export function getSalaIdentifier(sala: ISala): number | undefined {
  return sala.id;
}
