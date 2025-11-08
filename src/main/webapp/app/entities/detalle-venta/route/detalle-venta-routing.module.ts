import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetalleVentaComponent } from '../list/detalle-venta.component';
import { DetalleVentaDetailComponent } from '../detail/detalle-venta-detail.component';
import { DetalleVentaUpdateComponent } from '../update/detalle-venta-update.component';
import { DetalleVentaRoutingResolveService } from './detalle-venta-routing-resolve.service';

const detalleVentaRoute: Routes = [
  {
    path: '',
    component: DetalleVentaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetalleVentaDetailComponent,
    resolve: {
      detalleVenta: DetalleVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetalleVentaUpdateComponent,
    resolve: {
      detalleVenta: DetalleVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetalleVentaUpdateComponent,
    resolve: {
      detalleVenta: DetalleVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detalleVentaRoute)],
  exports: [RouterModule],
})
export class DetalleVentaRoutingModule {}
