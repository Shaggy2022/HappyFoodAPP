import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWorkDay, WorkDay } from '../work-day.model';
import { WorkDayService } from '../service/work-day.service';
import { IHorary } from 'app/entities/horary/horary.model';
import { HoraryService } from 'app/entities/horary/service/horary.service';
import { State } from 'app/entities/enumerations/state.model';

@Component({
  selector: 'happy-work-day-update',
  templateUrl: './work-day-update.component.html',
})
export class WorkDayUpdateComponent implements OnInit {
  isSaving = false;
  stateValues = Object.keys(State);

  horariesSharedCollection: IHorary[] = [];

  editForm = this.fb.group({
    id: [],
    dayName: [null, [Validators.required, Validators.maxLength(40)]],
    month: [null, [Validators.required, Validators.maxLength(40)]],
    state: [null, [Validators.required]],
    horary: [null, Validators.required],
  });

  constructor(
    protected workDayService: WorkDayService,
    protected horaryService: HoraryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workDay }) => {
      this.updateForm(workDay);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workDay = this.createFromForm();
    if (workDay.id !== undefined) {
      this.subscribeToSaveResponse(this.workDayService.update(workDay));
    } else {
      this.subscribeToSaveResponse(this.workDayService.create(workDay));
    }
  }

  trackHoraryById(_index: number, item: IHorary): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkDay>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workDay: IWorkDay): void {
    this.editForm.patchValue({
      id: workDay.id,
      dayName: workDay.dayName,
      month: workDay.month,
      state: workDay.state,
      horary: workDay.horary,
    });

    this.horariesSharedCollection = this.horaryService.addHoraryToCollectionIfMissing(this.horariesSharedCollection, workDay.horary);
  }

  protected loadRelationshipsOptions(): void {
    this.horaryService
      .query()
      .pipe(map((res: HttpResponse<IHorary[]>) => res.body ?? []))
      .pipe(map((horaries: IHorary[]) => this.horaryService.addHoraryToCollectionIfMissing(horaries, this.editForm.get('horary')!.value)))
      .subscribe((horaries: IHorary[]) => (this.horariesSharedCollection = horaries));
  }

  protected createFromForm(): IWorkDay {
    return {
      ...new WorkDay(),
      id: this.editForm.get(['id'])!.value,
      dayName: this.editForm.get(['dayName'])!.value,
      month: this.editForm.get(['month'])!.value,
      state: this.editForm.get(['state'])!.value,
      horary: this.editForm.get(['horary'])!.value,
    };
  }
}
