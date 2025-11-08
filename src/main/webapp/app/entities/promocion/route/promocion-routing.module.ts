import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PromocionComponent } from '../list/promocion.component';
import { PromocionDetailComponent } from '../detail/promocion-detail.component';
import { PromocionUpdateComponent } from '../update/promocion-update.component';
import { PromocionRoutingResolveService } from './promocion-routing-resolve.service';

const promocionRoute: Routes = [
  {
    path: '',
    component: PromocionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PromocionDetailComponent,
    resolve: {
      promocion: PromocionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PromocionUpdateComponent,
    resolve: {
      promocion: PromocionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PromocionUpdateComponent,
    resolve: {
      promocion: PromocionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promocionRoute)],
  exports: [RouterModule],
})
export class PromocionRoutingModule {}
