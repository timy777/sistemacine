import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPromocion, getPromocionIdentifier } from '../promocion.model';

export type EntityResponseType = HttpResponse<IPromocion>;
export type EntityArrayResponseType = HttpResponse<IPromocion[]>;

@Injectable({ providedIn: 'root' })
export class PromocionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promocions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(promocion: IPromocion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promocion);
    return this.http
      .post<IPromocion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(promocion: IPromocion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promocion);
    return this.http
      .put<IPromocion>(`${this.resourceUrl}/${getPromocionIdentifier(promocion) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(promocion: IPromocion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promocion);
    return this.http
      .patch<IPromocion>(`${this.resourceUrl}/${getPromocionIdentifier(promocion) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IPromocion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPromocion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPromocionToCollectionIfMissing(
    promocionCollection: IPromocion[],
    ...promocionsToCheck: (IPromocion | null | undefined)[]
  ): IPromocion[] {
    const promocions: IPromocion[] = promocionsToCheck.filter(isPresent);
    if (promocions.length > 0) {
      const promocionCollectionIdentifiers = promocionCollection.map(promocionItem => getPromocionIdentifier(promocionItem)!);
      const promocionsToAdd = promocions.filter(promocionItem => {
        const promocionIdentifier = getPromocionIdentifier(promocionItem);
        if (promocionIdentifier == null || promocionCollectionIdentifiers.includes(promocionIdentifier)) {
          return false;
        }
        promocionCollectionIdentifiers.push(promocionIdentifier);
        return true;
      });
      return [...promocionsToAdd, ...promocionCollection];
    }
    return promocionCollection;
  }

  protected convertDateFromClient(promocion: IPromocion): IPromocion {
    return Object.assign({}, promocion, {
      fechaInicio: promocion.fechaInicio?.isValid() ? promocion.fechaInicio.format(DATE_FORMAT) : undefined,
      fechaFin: promocion.fechaFin?.isValid() ? promocion.fechaFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaInicio = res.body.fechaInicio ? dayjs(res.body.fechaInicio) : undefined;
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((promocion: IPromocion) => {
        promocion.fechaInicio = promocion.fechaInicio ? dayjs(promocion.fechaInicio) : undefined;
        promocion.fechaFin = promocion.fechaFin ? dayjs(promocion.fechaFin) : undefined;
      });
    }
    return res;
  }
}
