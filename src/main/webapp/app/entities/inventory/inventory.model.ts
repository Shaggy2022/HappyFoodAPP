import { IProduct } from 'app/entities/product/product.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInventory {
  id?: number;
  amount?: number;
  description?: string | null;
  price?: number;
  products?: IProduct[] | null;
  invoice?: IInvoice;
}

export class Inventory implements IInventory {
  constructor(
    public id?: number,
    public amount?: number,
    public description?: string | null,
    public price?: number,
    public products?: IProduct[] | null,
    public invoice?: IInvoice
  ) {}
}

export function getInventoryIdentifier(inventory: IInventory): number | undefined {
  return inventory.id;
}
