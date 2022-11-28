import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHorary, getHoraryIdentifier } from '../horary.model';

export type EntityResponseType = HttpResponse<IHorary>;
export type EntityArrayResponseType = HttpResponse<IHorary[]>;

@Injectable({ providedIn: 'root' })
export class HoraryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/horaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(horary: IHorary): Observable<EntityResponseType> {
    return this.http.post<IHorary>(this.resourceUrl, horary, { observe: 'response' });
  }

  update(horary: IHorary): Observable<EntityResponseType> {
    return this.http.put<IHorary>(`${this.resourceUrl}/${getHoraryIdentifier(horary) as number}`, horary, { observe: 'response' });
  }

  partialUpdate(horary: IHorary): Observable<EntityResponseType> {
    return this.http.patch<IHorary>(`${this.resourceUrl}/${getHoraryIdentifier(horary) as number}`, horary, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHorary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHorary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHoraryToCollectionIfMissing(horaryCollection: IHorary[], ...horariesToCheck: (IHorary | null | undefined)[]): IHorary[] {
    const horaries: IHorary[] = horariesToCheck.filter(isPresent);
    if (horaries.length > 0) {
      const horaryCollectionIdentifiers = horaryCollection.map(horaryItem => getHoraryIdentifier(horaryItem)!);
      const horariesToAdd = horaries.filter(horaryItem => {
        const horaryIdentifier = getHoraryIdentifier(horaryItem);
        if (horaryIdentifier == null || horaryCollectionIdentifiers.includes(horaryIdentifier)) {
          return false;
        }
        horaryCollectionIdentifiers.push(horaryIdentifier);
        return true;
      });
      return [...horariesToAdd, ...horaryCollection];
    }
    return horaryCollection;
  }
}
