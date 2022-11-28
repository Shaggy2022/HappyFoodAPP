import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvoiceProductComponent } from './list/invoice-product.component';
import { InvoiceProductDetailComponent } from './detail/invoice-product-detail.component';
import { InvoiceProductUpdateComponent } from './update/invoice-product-update.component';
import { InvoiceProductDeleteDialogComponent } from './delete/invoice-product-delete-dialog.component';
import { InvoiceProductRoutingModule } from './route/invoice-product-routing.module';

@NgModule({
  imports: [SharedModule, InvoiceProductRoutingModule],
  declarations: [
    InvoiceProductComponent,
    InvoiceProductDetailComponent,
    InvoiceProductUpdateComponent,
    InvoiceProductDeleteDialogComponent,
  ],
  entryComponents: [InvoiceProductDeleteDialogComponent],
})
export class InvoiceProductModule {}
