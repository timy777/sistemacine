import dayjs from 'dayjs/esm';
import { TipoPersona } from 'app/entities/enumerations/tipo-persona.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';

export interface IPersona {
  id?: number;
  nombre?: string;
  apellido?: string;
  telefono?: string | null;
  email?: string;
  tipo?: TipoPersona;
  fechaNacimiento?: dayjs.Dayjs;
  sexo?: Sexo;
  carnetIdentidad?: string;
}

export class Persona implements IPersona {
  constructor(
    public id?: number,
    public nombre?: string,
    public apellido?: string,
    public telefono?: string | null,
    public email?: string,
    public tipo?: TipoPersona,
    public fechaNacimiento?: dayjs.Dayjs,
    public sexo?: Sexo,
    public carnetIdentidad?: string
  ) {}
}

export function getPersonaIdentifier(persona: IPersona): number | undefined {
  return persona.id;
}
