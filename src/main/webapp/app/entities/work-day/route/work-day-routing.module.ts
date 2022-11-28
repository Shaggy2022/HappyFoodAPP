import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkDayComponent } from '../list/work-day.component';
import { WorkDayDetailComponent } from '../detail/work-day-detail.component';
import { WorkDayUpdateComponent } from '../update/work-day-update.component';
import { WorkDayRoutingResolveService } from './work-day-routing-resolve.service';

const workDayRoute: Routes = [
  {
    path: '',
    component: WorkDayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkDayDetailComponent,
    resolve: {
      workDay: WorkDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkDayUpdateComponent,
    resolve: {
      workDay: WorkDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkDayUpdateComponent,
    resolve: {
      workDay: WorkDayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workDayRoute)],
  exports: [RouterModule],
})
export class WorkDayRoutingModule {}
