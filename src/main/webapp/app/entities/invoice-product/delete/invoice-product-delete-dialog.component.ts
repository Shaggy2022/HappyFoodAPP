import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceProduct } from '../invoice-product.model';
import { InvoiceProductService } from '../service/invoice-product.service';

@Component({
  templateUrl: './invoice-product-delete-dialog.component.html',
})
export class InvoiceProductDeleteDialogComponent {
  invoiceProduct?: IInvoiceProduct;

  constructor(protected invoiceProductService: InvoiceProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
