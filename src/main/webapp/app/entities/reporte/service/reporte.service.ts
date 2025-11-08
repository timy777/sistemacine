import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReporte, getReporteIdentifier } from '../reporte.model';

export type EntityResponseType = HttpResponse<IReporte>;
export type EntityArrayResponseType = HttpResponse<IReporte[]>;

@Injectable({ providedIn: 'root' })
export class ReporteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reportes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reporte: IReporte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporte);
    return this.http
      .post<IReporte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(reporte: IReporte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporte);
    return this.http
      .put<IReporte>(`${this.resourceUrl}/${getReporteIdentifier(reporte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(reporte: IReporte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reporte);
    return this.http
      .patch<IReporte>(`${this.resourceUrl}/${getReporteIdentifier(reporte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReporte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReporte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReporteToCollectionIfMissing(reporteCollection: IReporte[], ...reportesToCheck: (IReporte | null | undefined)[]): IReporte[] {
    const reportes: IReporte[] = reportesToCheck.filter(isPresent);
    if (reportes.length > 0) {
      const reporteCollectionIdentifiers = reporteCollection.map(reporteItem => getReporteIdentifier(reporteItem)!);
      const reportesToAdd = reportes.filter(reporteItem => {
        const reporteIdentifier = getReporteIdentifier(reporteItem);
        if (reporteIdentifier == null || reporteCollectionIdentifiers.includes(reporteIdentifier)) {
          return false;
        }
        reporteCollectionIdentifiers.push(reporteIdentifier);
        return true;
      });
      return [...reportesToAdd, ...reporteCollection];
    }
    return reporteCollection;
  }

  protected convertDateFromClient(reporte: IReporte): IReporte {
    return Object.assign({}, reporte, {
      fechaGeneracion: reporte.fechaGeneracion?.isValid() ? reporte.fechaGeneracion.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaGeneracion = res.body.fechaGeneracion ? dayjs(res.body.fechaGeneracion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reporte: IReporte) => {
        reporte.fechaGeneracion = reporte.fechaGeneracion ? dayjs(reporte.fechaGeneracion) : undefined;
      });
    }
    return res;
  }
}
