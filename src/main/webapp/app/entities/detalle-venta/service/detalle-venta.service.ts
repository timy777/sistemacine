import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalleVenta, getDetalleVentaIdentifier } from '../detalle-venta.model';

export type EntityResponseType = HttpResponse<IDetalleVenta>;
export type EntityArrayResponseType = HttpResponse<IDetalleVenta[]>;

@Injectable({ providedIn: 'root' })
export class DetalleVentaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalle-ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detalleVenta: IDetalleVenta): Observable<EntityResponseType> {
    return this.http.post<IDetalleVenta>(this.resourceUrl, detalleVenta, { observe: 'response' });
  }

  update(detalleVenta: IDetalleVenta): Observable<EntityResponseType> {
    return this.http.put<IDetalleVenta>(`${this.resourceUrl}/${getDetalleVentaIdentifier(detalleVenta) as number}`, detalleVenta, {
      observe: 'response',
    });
  }

  partialUpdate(detalleVenta: IDetalleVenta): Observable<EntityResponseType> {
    return this.http.patch<IDetalleVenta>(`${this.resourceUrl}/${getDetalleVentaIdentifier(detalleVenta) as number}`, detalleVenta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalleVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalleVenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetalleVentaToCollectionIfMissing(
    detalleVentaCollection: IDetalleVenta[],
    ...detalleVentasToCheck: (IDetalleVenta | null | undefined)[]
  ): IDetalleVenta[] {
    const detalleVentas: IDetalleVenta[] = detalleVentasToCheck.filter(isPresent);
    if (detalleVentas.length > 0) {
      const detalleVentaCollectionIdentifiers = detalleVentaCollection.map(
        detalleVentaItem => getDetalleVentaIdentifier(detalleVentaItem)!
      );
      const detalleVentasToAdd = detalleVentas.filter(detalleVentaItem => {
        const detalleVentaIdentifier = getDetalleVentaIdentifier(detalleVentaItem);
        if (detalleVentaIdentifier == null || detalleVentaCollectionIdentifiers.includes(detalleVentaIdentifier)) {
          return false;
        }
        detalleVentaCollectionIdentifiers.push(detalleVentaIdentifier);
        return true;
      });
      return [...detalleVentasToAdd, ...detalleVentaCollection];
    }
    return detalleVentaCollection;
  }
}
