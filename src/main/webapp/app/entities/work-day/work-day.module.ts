import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkDayComponent } from './list/work-day.component';
import { WorkDayDetailComponent } from './detail/work-day-detail.component';
import { WorkDayUpdateComponent } from './update/work-day-update.component';
import { WorkDayDeleteDialogComponent } from './delete/work-day-delete-dialog.component';
import { WorkDayRoutingModule } from './route/work-day-routing.module';

@NgModule({
  imports: [SharedModule, WorkDayRoutingModule],
  declarations: [WorkDayComponent, WorkDayDetailComponent, WorkDayUpdateComponent, WorkDayDeleteDialogComponent],
  entryComponents: [WorkDayDeleteDialogComponent],
})
export class WorkDayModule {}
