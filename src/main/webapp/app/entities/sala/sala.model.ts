import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';

export interface ISala {
  id?: string;
  nombre?: string;
  capacidad?: number;
  tipo?: TipoSala;
  estado?: string | null;
}

export class Sala implements ISala {
  constructor(
    public id?: string,
    public nombre?: string,
    public capacidad?: number,
    public tipo?: TipoSala,
    public estado?: string | null
  ) {}
}

export function getSalaIdentifier(sala: ISala): string | undefined {
  return sala.id;
}
