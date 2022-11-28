import { IInvoiceProduct } from 'app/entities/invoice-product/invoice-product.model';
import { IInventory } from 'app/entities/inventory/inventory.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: number;
  iva?: number;
  totalToPay?: number;
  invoiceProducts?: IInvoiceProduct[] | null;
  inventories?: IInventory[] | null;
  order?: IOrder;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: number,
    public iva?: number,
    public totalToPay?: number,
    public invoiceProducts?: IInvoiceProduct[] | null,
    public inventories?: IInventory[] | null,
    public order?: IOrder
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
