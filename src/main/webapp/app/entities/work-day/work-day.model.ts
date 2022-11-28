import { IEmployee } from 'app/entities/employee/employee.model';
import { IHorary } from 'app/entities/horary/horary.model';
import { State } from 'app/entities/enumerations/state.model';

export interface IWorkDay {
  id?: number;
  dayName?: string;
  month?: string;
  state?: State;
  employees?: IEmployee[] | null;
  horary?: IHorary;
}

export class WorkDay implements IWorkDay {
  constructor(
    public id?: number,
    public dayName?: string,
    public month?: string,
    public state?: State,
    public employees?: IEmployee[] | null,
    public horary?: IHorary
  ) {}
}

export function getWorkDayIdentifier(workDay: IWorkDay): number | undefined {
  return workDay.id;
}
