import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagerComponent } from '../list/manager.component';
import { ManagerDetailComponent } from '../detail/manager-detail.component';
import { ManagerUpdateComponent } from '../update/manager-update.component';
import { ManagerRoutingResolveService } from './manager-routing-resolve.service';
import { Authority } from '../../../config/authority.constants';

const managerRoute: Routes = [
  {
    path: '',
    component: ManagerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManagerDetailComponent,
    resolve: {
      manager: ManagerRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManagerUpdateComponent,
    resolve: {
      manager: ManagerRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManagerUpdateComponent,
    resolve: {
      manager: ManagerRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(managerRoute)],
  exports: [RouterModule],
})
export class ManagerRoutingModule {}
