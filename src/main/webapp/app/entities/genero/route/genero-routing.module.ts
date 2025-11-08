import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GeneroComponent } from '../list/genero.component';
import { GeneroDetailComponent } from '../detail/genero-detail.component';
import { GeneroUpdateComponent } from '../update/genero-update.component';
import { GeneroRoutingResolveService } from './genero-routing-resolve.service';

const generoRoute: Routes = [
  {
    path: '',
    component: GeneroComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GeneroDetailComponent,
    resolve: {
      genero: GeneroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GeneroUpdateComponent,
    resolve: {
      genero: GeneroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GeneroUpdateComponent,
    resolve: {
      genero: GeneroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(generoRoute)],
  exports: [RouterModule],
})
export class GeneroRoutingModule {}
