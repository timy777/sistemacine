import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGenero, Genero } from '../genero.model';
import { GeneroService } from '../service/genero.service';

@Injectable({ providedIn: 'root' })
export class GeneroRoutingResolveService implements Resolve<IGenero> {
  constructor(protected service: GeneroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGenero> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((genero: HttpResponse<Genero>) => {
          if (genero.body) {
            return of(genero.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Genero());
  }
}
