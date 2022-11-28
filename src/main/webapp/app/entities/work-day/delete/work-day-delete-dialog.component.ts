import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkDay } from '../work-day.model';
import { WorkDayService } from '../service/work-day.service';

@Component({
  templateUrl: './work-day-delete-dialog.component.html',
})
export class WorkDayDeleteDialogComponent {
  workDay?: IWorkDay;

  constructor(protected workDayService: WorkDayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workDayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
