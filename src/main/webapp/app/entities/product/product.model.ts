import { IInvoiceProduct } from 'app/entities/invoice-product/invoice-product.model';
import { IInventory } from 'app/entities/inventory/inventory.model';

export interface IProduct {
  id?: number;
  serial?: string;
  requiredProduct?: string;
  price?: number;
  invoiceProducts?: IInvoiceProduct[] | null;
  inventory?: IInventory;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public serial?: string,
    public requiredProduct?: string,
    public price?: number,
    public invoiceProducts?: IInvoiceProduct[] | null,
    public inventory?: IInventory
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
