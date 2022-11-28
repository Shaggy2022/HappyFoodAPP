import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInvoiceProduct {
  id?: number;
  date?: dayjs.Dayjs;
  product?: IProduct;
  invoice?: IInvoice;
}

export class InvoiceProduct implements IInvoiceProduct {
  constructor(public id?: number, public date?: dayjs.Dayjs, public product?: IProduct, public invoice?: IInvoice) {}
}

export function getInvoiceProductIdentifier(invoiceProduct: IInvoiceProduct): number | undefined {
  return invoiceProduct.id;
}
