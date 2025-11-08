import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITarifa, getTarifaIdentifier } from '../tarifa.model';

export type EntityResponseType = HttpResponse<ITarifa>;
export type EntityArrayResponseType = HttpResponse<ITarifa[]>;

@Injectable({ providedIn: 'root' })
export class TarifaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tarifas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tarifa: ITarifa): Observable<EntityResponseType> {
    return this.http.post<ITarifa>(this.resourceUrl, tarifa, { observe: 'response' });
  }

  update(tarifa: ITarifa): Observable<EntityResponseType> {
    return this.http.put<ITarifa>(`${this.resourceUrl}/${getTarifaIdentifier(tarifa) as number}`, tarifa, { observe: 'response' });
  }

  partialUpdate(tarifa: ITarifa): Observable<EntityResponseType> {
    return this.http.patch<ITarifa>(`${this.resourceUrl}/${getTarifaIdentifier(tarifa) as number}`, tarifa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITarifa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITarifa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTarifaToCollectionIfMissing(tarifaCollection: ITarifa[], ...tarifasToCheck: (ITarifa | null | undefined)[]): ITarifa[] {
    const tarifas: ITarifa[] = tarifasToCheck.filter(isPresent);
    if (tarifas.length > 0) {
      const tarifaCollectionIdentifiers = tarifaCollection.map(tarifaItem => getTarifaIdentifier(tarifaItem)!);
      const tarifasToAdd = tarifas.filter(tarifaItem => {
        const tarifaIdentifier = getTarifaIdentifier(tarifaItem);
        if (tarifaIdentifier == null || tarifaCollectionIdentifiers.includes(tarifaIdentifier)) {
          return false;
        }
        tarifaCollectionIdentifiers.push(tarifaIdentifier);
        return true;
      });
      return [...tarifasToAdd, ...tarifaCollection];
    }
    return tarifaCollection;
  }
}
