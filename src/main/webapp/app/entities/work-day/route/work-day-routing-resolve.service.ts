import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkDay, WorkDay } from '../work-day.model';
import { WorkDayService } from '../service/work-day.service';

@Injectable({ providedIn: 'root' })
export class WorkDayRoutingResolveService implements Resolve<IWorkDay> {
  constructor(protected service: WorkDayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkDay> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workDay: HttpResponse<WorkDay>) => {
          if (workDay.body) {
            return of(workDay.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkDay());
  }
}
