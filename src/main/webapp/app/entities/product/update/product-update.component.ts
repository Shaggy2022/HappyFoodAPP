import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { IInventory } from 'app/entities/inventory/inventory.model';
import { InventoryService } from 'app/entities/inventory/service/inventory.service';

@Component({
  selector: 'happy-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  inventoriesSharedCollection: IInventory[] = [];

  editForm = this.fb.group({
    id: [],
    serial: [null, [Validators.required, Validators.maxLength(255)]],
    requiredProduct: [null, [Validators.required, Validators.maxLength(200)]],
    price: [null, [Validators.required]],
    inventory: [null, Validators.required],
  });

  constructor(
    protected productService: ProductService,
    protected inventoryService: InventoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackInventoryById(_index: number, item: IInventory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      serial: product.serial,
      requiredProduct: product.requiredProduct,
      price: product.price,
      inventory: product.inventory,
    });

    this.inventoriesSharedCollection = this.inventoryService.addInventoryToCollectionIfMissing(
      this.inventoriesSharedCollection,
      product.inventory
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryService
      .query()
      .pipe(map((res: HttpResponse<IInventory[]>) => res.body ?? []))
      .pipe(
        map((inventories: IInventory[]) =>
          this.inventoryService.addInventoryToCollectionIfMissing(inventories, this.editForm.get('inventory')!.value)
        )
      )
      .subscribe((inventories: IInventory[]) => (this.inventoriesSharedCollection = inventories));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      serial: this.editForm.get(['serial'])!.value,
      requiredProduct: this.editForm.get(['requiredProduct'])!.value,
      price: this.editForm.get(['price'])!.value,
      inventory: this.editForm.get(['inventory'])!.value,
    };
  }
}
