import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvoiceProduct, InvoiceProduct } from '../invoice-product.model';
import { InvoiceProductService } from '../service/invoice-product.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

@Component({
  selector: 'happy-invoice-product-update',
  templateUrl: './invoice-product-update.component.html',
})
export class InvoiceProductUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  invoicesSharedCollection: IInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    product: [null, Validators.required],
    invoice: [null, Validators.required],
  });

  constructor(
    protected invoiceProductService: InvoiceProductService,
    protected productService: ProductService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceProduct }) => {
      this.updateForm(invoiceProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceProduct = this.createFromForm();
    if (invoiceProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceProductService.update(invoiceProduct));
    } else {
      this.subscribeToSaveResponse(this.invoiceProductService.create(invoiceProduct));
    }
  }

  trackProductById(_index: number, item: IProduct): number {
    return item.id!;
  }

  trackInvoiceById(_index: number, item: IInvoice): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(invoiceProduct: IInvoiceProduct): void {
    this.editForm.patchValue({
      id: invoiceProduct.id,
      date: invoiceProduct.date,
      product: invoiceProduct.product,
      invoice: invoiceProduct.invoice,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      invoiceProduct.product
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(
      this.invoicesSharedCollection,
      invoiceProduct.invoice
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing(invoices, this.editForm.get('invoice')!.value))
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }

  protected createFromForm(): IInvoiceProduct {
    return {
      ...new InvoiceProduct(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      product: this.editForm.get(['product'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
    };
  }
}
