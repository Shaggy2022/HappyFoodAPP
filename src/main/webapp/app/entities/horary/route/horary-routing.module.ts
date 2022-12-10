import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HoraryComponent } from '../list/horary.component';
import { HoraryDetailComponent } from '../detail/horary-detail.component';
import { HoraryUpdateComponent } from '../update/horary-update.component';
import { HoraryRoutingResolveService } from './horary-routing-resolve.service';
import { Authority } from '../../../config/authority.constants';

const horaryRoute: Routes = [
  {
    path: '',
    component: HoraryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HoraryDetailComponent,
    resolve: {
      horary: HoraryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HoraryUpdateComponent,
    resolve: {
      horary: HoraryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HoraryUpdateComponent,
    resolve: {
      horary: HoraryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(horaryRoute)],
  exports: [RouterModule],
})
export class HoraryRoutingModule {}
