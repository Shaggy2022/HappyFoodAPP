import { ICustomer } from 'app/entities/customer/customer.model';
import { State } from 'app/entities/enumerations/state.model';

export interface IDocumentType {
  id?: number;
  initials?: string;
  documentName?: string;
  stateDocumentType?: State;
  customers?: ICustomer[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(
    public id?: number,
    public initials?: string,
    public documentName?: string,
    public stateDocumentType?: State,
    public customers?: ICustomer[] | null
  ) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
