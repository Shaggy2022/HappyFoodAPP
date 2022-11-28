import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkDay } from 'app/entities/work-day/work-day.model';
import { WorkDayService } from 'app/entities/work-day/service/work-day.service';
import { IManager } from 'app/entities/manager/manager.model';
import { ManagerService } from 'app/entities/manager/service/manager.service';
import { State } from 'app/entities/enumerations/state.model';

@Component({
  selector: 'happy-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  stateValues = Object.keys(State);

  usersSharedCollection: IUser[] = [];
  workDaysSharedCollection: IWorkDay[] = [];
  managersSharedCollection: IManager[] = [];

  editForm = this.fb.group({
    id: [],
    documentNumber: [null, [Validators.required, Validators.maxLength(50)]],
    firstName: [null, [Validators.required, Validators.maxLength(50)]],
    secondName: [null, [Validators.maxLength(50)]],
    firstLastname: [null, [Validators.required, Validators.maxLength(50)]],
    secondLastname: [null, [Validators.maxLength(50)]],
    position: [null, [Validators.required, Validators.maxLength(100)]],
    phoneNumber: [null, [Validators.required, Validators.maxLength(100)]],
    address: [null, [Validators.required, Validators.maxLength(100)]],
    state: [null, [Validators.required]],
    user: [null, Validators.required],
    workDay: [null, Validators.required],
    manager: [null, Validators.required],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected workDayService: WorkDayService,
    protected managerService: ManagerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkDayById(_index: number, item: IWorkDay): number {
    return item.id!;
  }

  trackManagerById(_index: number, item: IManager): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      documentNumber: employee.documentNumber,
      firstName: employee.firstName,
      secondName: employee.secondName,
      firstLastname: employee.firstLastname,
      secondLastname: employee.secondLastname,
      position: employee.position,
      phoneNumber: employee.phoneNumber,
      address: employee.address,
      state: employee.state,
      user: employee.user,
      workDay: employee.workDay,
      manager: employee.manager,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.user);
    this.workDaysSharedCollection = this.workDayService.addWorkDayToCollectionIfMissing(this.workDaysSharedCollection, employee.workDay);
    this.managersSharedCollection = this.managerService.addManagerToCollectionIfMissing(this.managersSharedCollection, employee.manager);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.workDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkDay[]>) => res.body ?? []))
      .pipe(
        map((workDays: IWorkDay[]) => this.workDayService.addWorkDayToCollectionIfMissing(workDays, this.editForm.get('workDay')!.value))
      )
      .subscribe((workDays: IWorkDay[]) => (this.workDaysSharedCollection = workDays));

    this.managerService
      .query()
      .pipe(map((res: HttpResponse<IManager[]>) => res.body ?? []))
      .pipe(
        map((managers: IManager[]) => this.managerService.addManagerToCollectionIfMissing(managers, this.editForm.get('manager')!.value))
      )
      .subscribe((managers: IManager[]) => (this.managersSharedCollection = managers));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      documentNumber: this.editForm.get(['documentNumber'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      secondName: this.editForm.get(['secondName'])!.value,
      firstLastname: this.editForm.get(['firstLastname'])!.value,
      secondLastname: this.editForm.get(['secondLastname'])!.value,
      position: this.editForm.get(['position'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      state: this.editForm.get(['state'])!.value,
      user: this.editForm.get(['user'])!.value,
      workDay: this.editForm.get(['workDay'])!.value,
      manager: this.editForm.get(['manager'])!.value,
    };
  }
}
