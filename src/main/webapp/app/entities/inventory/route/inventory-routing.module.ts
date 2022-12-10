import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventoryComponent } from '../list/inventory.component';
import { InventoryDetailComponent } from '../detail/inventory-detail.component';
import { InventoryUpdateComponent } from '../update/inventory-update.component';
import { InventoryRoutingResolveService } from './inventory-routing-resolve.service';
import { Authority } from '../../../config/authority.constants';

const inventoryRoute: Routes = [
  {
    path: '',
    component: InventoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventoryDetailComponent,
    resolve: {
      inventory: InventoryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventoryUpdateComponent,
    resolve: {
      inventory: InventoryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventoryUpdateComponent,
    resolve: {
      inventory: InventoryRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inventoryRoute)],
  exports: [RouterModule],
})
export class InventoryRoutingModule {}
