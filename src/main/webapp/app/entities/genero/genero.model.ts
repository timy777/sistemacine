export interface IGenero {
  id?: string;
  nombre?: string;
  descripcion?: string | null;
}

export class Genero implements IGenero {
  constructor(public id?: string, public nombre?: string, public descripcion?: string | null) {}
}

export function getGeneroIdentifier(genero: IGenero): string | undefined {
  return genero.id;
}
