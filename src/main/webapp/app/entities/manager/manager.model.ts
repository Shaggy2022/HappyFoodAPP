import { IUser } from 'app/entities/user/user.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IManager {
  id?: number;
  documentNumber?: string;
  firstName?: string;
  secondName?: string | null;
  firstLastname?: string;
  secondLastname?: string | null;
  user?: IUser;
  employees?: IEmployee[] | null;
}

export class Manager implements IManager {
  constructor(
    public id?: number,
    public documentNumber?: string,
    public firstName?: string,
    public secondName?: string | null,
    public firstLastname?: string,
    public secondLastname?: string | null,
    public user?: IUser,
    public employees?: IEmployee[] | null
  ) {}
}

export function getManagerIdentifier(manager: IManager): number | undefined {
  return manager.id;
}
