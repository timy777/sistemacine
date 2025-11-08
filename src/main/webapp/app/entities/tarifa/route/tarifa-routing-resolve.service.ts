import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITarifa, Tarifa } from '../tarifa.model';
import { TarifaService } from '../service/tarifa.service';

@Injectable({ providedIn: 'root' })
export class TarifaRoutingResolveService implements Resolve<ITarifa> {
  constructor(protected service: TarifaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITarifa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tarifa: HttpResponse<Tarifa>) => {
          if (tarifa.body) {
            return of(tarifa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tarifa());
  }
}
