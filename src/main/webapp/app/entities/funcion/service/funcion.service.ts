import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncion, getFuncionIdentifier } from '../funcion.model';

export type EntityResponseType = HttpResponse<IFuncion>;
export type EntityArrayResponseType = HttpResponse<IFuncion[]>;

@Injectable({ providedIn: 'root' })
export class FuncionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/funcions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(funcion: IFuncion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcion);
    return this.http
      .post<IFuncion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(funcion: IFuncion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcion);
    return this.http
      .put<IFuncion>(`${this.resourceUrl}/${getFuncionIdentifier(funcion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(funcion: IFuncion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcion);
    return this.http
      .patch<IFuncion>(`${this.resourceUrl}/${getFuncionIdentifier(funcion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFuncion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFuncion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFuncionToCollectionIfMissing(funcionCollection: IFuncion[], ...funcionsToCheck: (IFuncion | null | undefined)[]): IFuncion[] {
    const funcions: IFuncion[] = funcionsToCheck.filter(isPresent);
    if (funcions.length > 0) {
      const funcionCollectionIdentifiers = funcionCollection.map(funcionItem => getFuncionIdentifier(funcionItem)!);
      const funcionsToAdd = funcions.filter(funcionItem => {
        const funcionIdentifier = getFuncionIdentifier(funcionItem);
        if (funcionIdentifier == null || funcionCollectionIdentifiers.includes(funcionIdentifier)) {
          return false;
        }
        funcionCollectionIdentifiers.push(funcionIdentifier);
        return true;
      });
      return [...funcionsToAdd, ...funcionCollection];
    }
    return funcionCollection;
  }

  protected convertDateFromClient(funcion: IFuncion): IFuncion {
    return Object.assign({}, funcion, {
      fecha: funcion.fecha?.isValid() ? funcion.fecha.format(DATE_FORMAT) : undefined,
      horaInicio: funcion.horaInicio?.isValid() ? funcion.horaInicio.toJSON() : undefined,
      horaFin: funcion.horaFin?.isValid() ? funcion.horaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
      res.body.horaInicio = res.body.horaInicio ? dayjs(res.body.horaInicio) : undefined;
      res.body.horaFin = res.body.horaFin ? dayjs(res.body.horaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((funcion: IFuncion) => {
        funcion.fecha = funcion.fecha ? dayjs(funcion.fecha) : undefined;
        funcion.horaInicio = funcion.horaInicio ? dayjs(funcion.horaInicio) : undefined;
        funcion.horaFin = funcion.horaFin ? dayjs(funcion.horaFin) : undefined;
      });
    }
    return res;
  }
}
