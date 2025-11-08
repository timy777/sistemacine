export interface IGenero {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export class Genero implements IGenero {
  constructor(public id?: number, public nombre?: string, public descripcion?: string | null) {}
}

export function getGeneroIdentifier(genero: IGenero): number | undefined {
  return genero.id;
}
