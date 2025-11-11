import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGenero, getGeneroIdentifier } from '../genero.model';

export type EntityResponseType = HttpResponse<IGenero>;
export type EntityArrayResponseType = HttpResponse<IGenero[]>;

@Injectable({ providedIn: 'root' })
export class GeneroService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/generos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(genero: IGenero): Observable<EntityResponseType> {
    return this.http.post<IGenero>(this.resourceUrl, genero, { observe: 'response' });
  }

  update(genero: IGenero): Observable<EntityResponseType> {
    return this.http.put<IGenero>(`${this.resourceUrl}/${getGeneroIdentifier(genero) as string}`, genero, { observe: 'response' });
  }

  partialUpdate(genero: IGenero): Observable<EntityResponseType> {
    return this.http.patch<IGenero>(`${this.resourceUrl}/${getGeneroIdentifier(genero) as string}`, genero, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IGenero>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGenero[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGeneroToCollectionIfMissing(generoCollection: IGenero[], ...generosToCheck: (IGenero | null | undefined)[]): IGenero[] {
    const generos: IGenero[] = generosToCheck.filter(isPresent);
    if (generos.length > 0) {
      const generoCollectionIdentifiers = generoCollection.map(generoItem => getGeneroIdentifier(generoItem)!);
      const generosToAdd = generos.filter(generoItem => {
        const generoIdentifier = getGeneroIdentifier(generoItem);
        if (generoIdentifier == null || generoCollectionIdentifiers.includes(generoIdentifier)) {
          return false;
        }
        generoCollectionIdentifiers.push(generoIdentifier);
        return true;
      });
      return [...generosToAdd, ...generoCollection];
    }
    return generoCollection;
  }
}
