import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPromocion, Promocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';

@Injectable({ providedIn: 'root' })
export class PromocionRoutingResolveService implements Resolve<IPromocion> {
  constructor(protected service: PromocionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromocion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((promocion: HttpResponse<Promocion>) => {
          if (promocion.body) {
            return of(promocion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Promocion());
  }
}
