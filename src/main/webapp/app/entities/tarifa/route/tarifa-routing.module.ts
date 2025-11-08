import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TarifaComponent } from '../list/tarifa.component';
import { TarifaDetailComponent } from '../detail/tarifa-detail.component';
import { TarifaUpdateComponent } from '../update/tarifa-update.component';
import { TarifaRoutingResolveService } from './tarifa-routing-resolve.service';

const tarifaRoute: Routes = [
  {
    path: '',
    component: TarifaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TarifaDetailComponent,
    resolve: {
      tarifa: TarifaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TarifaUpdateComponent,
    resolve: {
      tarifa: TarifaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TarifaUpdateComponent,
    resolve: {
      tarifa: TarifaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tarifaRoute)],
  exports: [RouterModule],
})
export class TarifaRoutingModule {}
