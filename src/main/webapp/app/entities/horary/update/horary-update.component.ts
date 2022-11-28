import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHorary, Horary } from '../horary.model';
import { HoraryService } from '../service/horary.service';

@Component({
  selector: 'happy-horary-update',
  templateUrl: './horary-update.component.html',
})
export class HoraryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    startTime: [null, [Validators.required]],
    endTime: [null, [Validators.required]],
  });

  constructor(protected horaryService: HoraryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horary }) => {
      this.updateForm(horary);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horary = this.createFromForm();
    if (horary.id !== undefined) {
      this.subscribeToSaveResponse(this.horaryService.update(horary));
    } else {
      this.subscribeToSaveResponse(this.horaryService.create(horary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHorary>>): void {
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

  protected updateForm(horary: IHorary): void {
    this.editForm.patchValue({
      id: horary.id,
      startTime: horary.startTime,
      endTime: horary.endTime,
    });
  }

  protected createFromForm(): IHorary {
    return {
      ...new Horary(),
      id: this.editForm.get(['id'])!.value,
      startTime: this.editForm.get(['startTime'])!.value,
      endTime: this.editForm.get(['endTime'])!.value,
    };
  }
}
