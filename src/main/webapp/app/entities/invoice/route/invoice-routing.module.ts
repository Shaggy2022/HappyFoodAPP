import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceComponent } from '../list/invoice.component';
import { InvoiceDetailComponent } from '../detail/invoice-detail.component';
import { InvoiceUpdateComponent } from '../update/invoice-update.component';
import { InvoiceRoutingResolveService } from './invoice-routing-resolve.service';
import { Authority } from '../../../config/authority.constants';

const invoiceRoute: Routes = [
  {
    path: '',
    component: InvoiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceDetailComponent,
    resolve: {
      invoice: InvoiceRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE, Authority.CUSTOMER],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceUpdateComponent,
    resolve: {
      invoice: InvoiceRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceUpdateComponent,
    resolve: {
      invoice: InvoiceRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceRoute)],
  exports: [RouterModule],
})
export class InvoiceRoutingModule {}
