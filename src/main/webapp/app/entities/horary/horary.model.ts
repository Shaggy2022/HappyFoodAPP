import { IWorkDay } from 'app/entities/work-day/work-day.model';

export interface IHorary {
  id?: number;
  startTime?: string;
  endTime?: string;
  workDays?: IWorkDay[] | null;
}

export class Horary implements IHorary {
  constructor(public id?: number, public startTime?: string, public endTime?: string, public workDays?: IWorkDay[] | null) {}
}

export function getHoraryIdentifier(horary: IHorary): number | undefined {
  return horary.id;
}
