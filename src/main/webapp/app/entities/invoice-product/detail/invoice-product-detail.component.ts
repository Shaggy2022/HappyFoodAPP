import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceProduct } from '../invoice-product.model';

@Component({
  selector: 'happy-invoice-product-detail',
  templateUrl: './invoice-product-detail.component.html',
})
export class InvoiceProductDetailComponent implements OnInit {
  invoiceProduct: IInvoiceProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceProduct }) => {
      this.invoiceProduct = invoiceProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
