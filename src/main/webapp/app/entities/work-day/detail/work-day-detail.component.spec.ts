import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkDayDetailComponent } from './work-day-detail.component';

describe('WorkDay Management Detail Component', () => {
  let comp: WorkDayDetailComponent;
  let fixture: ComponentFixture<WorkDayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkDayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workDay: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkDayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkDayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workDay on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workDay).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
