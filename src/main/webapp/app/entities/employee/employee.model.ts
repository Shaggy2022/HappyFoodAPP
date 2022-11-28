import { IUser } from 'app/entities/user/user.model';
import { IWorkDay } from 'app/entities/work-day/work-day.model';
import { IManager } from 'app/entities/manager/manager.model';
import { State } from 'app/entities/enumerations/state.model';

export interface IEmployee {
  id?: number;
  documentNumber?: string;
  firstName?: string;
  secondName?: string | null;
  firstLastname?: string;
  secondLastname?: string | null;
  position?: string;
  phoneNumber?: string;
  address?: string;
  state?: State;
  user?: IUser;
  workDay?: IWorkDay;
  manager?: IManager;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public documentNumber?: string,
    public firstName?: string,
    public secondName?: string | null,
    public firstLastname?: string,
    public secondLastname?: string | null,
    public position?: string,
    public phoneNumber?: string,
    public address?: string,
    public state?: State,
    public user?: IUser,
    public workDay?: IWorkDay,
    public manager?: IManager
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
