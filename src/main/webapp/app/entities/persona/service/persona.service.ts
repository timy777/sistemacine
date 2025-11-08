import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersona, getPersonaIdentifier } from '../persona.model';

export type EntityResponseType = HttpResponse<IPersona>;
export type EntityArrayResponseType = HttpResponse<IPersona[]>;

@Injectable({ providedIn: 'root' })
export class PersonaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(persona: IPersona): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(persona);
    return this.http
      .post<IPersona>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(persona: IPersona): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(persona);
    return this.http
      .put<IPersona>(`${this.resourceUrl}/${getPersonaIdentifier(persona) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(persona: IPersona): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(persona);
    return this.http
      .patch<IPersona>(`${this.resourceUrl}/${getPersonaIdentifier(persona) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersona>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersona[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonaToCollectionIfMissing(personaCollection: IPersona[], ...personasToCheck: (IPersona | null | undefined)[]): IPersona[] {
    const personas: IPersona[] = personasToCheck.filter(isPresent);
    if (personas.length > 0) {
      const personaCollectionIdentifiers = personaCollection.map(personaItem => getPersonaIdentifier(personaItem)!);
      const personasToAdd = personas.filter(personaItem => {
        const personaIdentifier = getPersonaIdentifier(personaItem);
        if (personaIdentifier == null || personaCollectionIdentifiers.includes(personaIdentifier)) {
          return false;
        }
        personaCollectionIdentifiers.push(personaIdentifier);
        return true;
      });
      return [...personasToAdd, ...personaCollection];
    }
    return personaCollection;
  }

  protected convertDateFromClient(persona: IPersona): IPersona {
    return Object.assign({}, persona, {
      fechaNacimiento: persona.fechaNacimiento?.isValid() ? persona.fechaNacimiento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((persona: IPersona) => {
        persona.fechaNacimiento = persona.fechaNacimiento ? dayjs(persona.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
