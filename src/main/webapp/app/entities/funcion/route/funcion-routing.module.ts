import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FuncionComponent } from '../list/funcion.component';
import { FuncionDetailComponent } from '../detail/funcion-detail.component';
import { FuncionUpdateComponent } from '../update/funcion-update.component';
import { FuncionRoutingResolveService } from './funcion-routing-resolve.service';

const funcionRoute: Routes = [
  {
    path: '',
    component: FuncionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FuncionDetailComponent,
    resolve: {
      funcion: FuncionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncionUpdateComponent,
    resolve: {
      funcion: FuncionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncionUpdateComponent,
    resolve: {
      funcion: FuncionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(funcionRoute)],
  exports: [RouterModule],
})
export class FuncionRoutingModule {}
