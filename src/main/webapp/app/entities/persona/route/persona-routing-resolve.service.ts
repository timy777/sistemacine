import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersona, Persona } from '../persona.model';
import { PersonaService } from '../service/persona.service';

@Injectable({ providedIn: 'root' })
export class PersonaRoutingResolveService implements Resolve<IPersona> {
  constructor(protected service: PersonaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersona> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((persona: HttpResponse<Persona>) => {
          if (persona.body) {
            return of(persona.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Persona());
  }
}
