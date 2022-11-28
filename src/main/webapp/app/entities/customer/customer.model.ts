import { IUser } from 'app/entities/user/user.model';
import { IOrder } from 'app/entities/order/order.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';

export interface ICustomer {
  id?: number;
  firstName?: string;
  secondName?: string | null;
  firstLastname?: string;
  secondLastname?: string | null;
  address?: string;
  phoneNumber?: string;
  documentNumber?: string;
  user?: IUser;
  orders?: IOrder[] | null;
  documentType?: IDocumentType;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string,
    public secondName?: string | null,
    public firstLastname?: string,
    public secondLastname?: string | null,
    public address?: string,
    public phoneNumber?: string,
    public documentNumber?: string,
    public user?: IUser,
    public orders?: IOrder[] | null,
    public documentType?: IDocumentType
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
