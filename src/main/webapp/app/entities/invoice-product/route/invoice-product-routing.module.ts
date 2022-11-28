import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceProductComponent } from '../list/invoice-product.component';
import { InvoiceProductDetailComponent } from '../detail/invoice-product-detail.component';
import { InvoiceProductUpdateComponent } from '../update/invoice-product-update.component';
import { InvoiceProductRoutingResolveService } from './invoice-product-routing-resolve.service';

const invoiceProductRoute: Routes = [
  {
    path: '',
    component: InvoiceProductComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceProductDetailComponent,
    resolve: {
      invoiceProduct: InvoiceProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceProductUpdateComponent,
    resolve: {
      invoiceProduct: InvoiceProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceProductUpdateComponent,
    resolve: {
      invoiceProduct: InvoiceProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceProductRoute)],
  exports: [RouterModule],
})
export class InvoiceProductRoutingModule {}
