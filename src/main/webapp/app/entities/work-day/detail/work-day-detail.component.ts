import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkDay } from '../work-day.model';

@Component({
  selector: 'happy-work-day-detail',
  templateUrl: './work-day-detail.component.html',
})
export class WorkDayDetailComponent implements OnInit {
  workDay: IWorkDay | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workDay }) => {
      this.workDay = workDay;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
