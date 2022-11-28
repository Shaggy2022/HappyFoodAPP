import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkDay, getWorkDayIdentifier } from '../work-day.model';

export type EntityResponseType = HttpResponse<IWorkDay>;
export type EntityArrayResponseType = HttpResponse<IWorkDay[]>;

@Injectable({ providedIn: 'root' })
export class WorkDayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-days');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workDay: IWorkDay): Observable<EntityResponseType> {
    return this.http.post<IWorkDay>(this.resourceUrl, workDay, { observe: 'response' });
  }

  update(workDay: IWorkDay): Observable<EntityResponseType> {
    return this.http.put<IWorkDay>(`${this.resourceUrl}/${getWorkDayIdentifier(workDay) as number}`, workDay, { observe: 'response' });
  }

  partialUpdate(workDay: IWorkDay): Observable<EntityResponseType> {
    return this.http.patch<IWorkDay>(`${this.resourceUrl}/${getWorkDayIdentifier(workDay) as number}`, workDay, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkDay>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkDay[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkDayToCollectionIfMissing(workDayCollection: IWorkDay[], ...workDaysToCheck: (IWorkDay | null | undefined)[]): IWorkDay[] {
    const workDays: IWorkDay[] = workDaysToCheck.filter(isPresent);
    if (workDays.length > 0) {
      const workDayCollectionIdentifiers = workDayCollection.map(workDayItem => getWorkDayIdentifier(workDayItem)!);
      const workDaysToAdd = workDays.filter(workDayItem => {
        const workDayIdentifier = getWorkDayIdentifier(workDayItem);
        if (workDayIdentifier == null || workDayCollectionIdentifiers.includes(workDayIdentifier)) {
          return false;
        }
        workDayCollectionIdentifiers.push(workDayIdentifier);
        return true;
      });
      return [...workDaysToAdd, ...workDayCollection];
    }
    return workDayCollection;
  }
}
