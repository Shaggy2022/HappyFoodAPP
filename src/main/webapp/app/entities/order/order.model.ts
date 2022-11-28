import dayjs from 'dayjs/esm';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IOrder {
  id?: number;
  orderRequired?: string;
  date?: dayjs.Dayjs;
  amount?: number;
  fullOrderValue?: number;
  invoices?: IInvoice[] | null;
  customer?: ICustomer;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public orderRequired?: string,
    public date?: dayjs.Dayjs,
    public amount?: number,
    public fullOrderValue?: number,
    public invoices?: IInvoice[] | null,
    public customer?: ICustomer
  ) {}
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
