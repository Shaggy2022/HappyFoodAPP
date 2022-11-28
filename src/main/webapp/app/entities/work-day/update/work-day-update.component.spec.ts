import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkDayService } from '../service/work-day.service';
import { IWorkDay, WorkDay } from '../work-day.model';
import { IHorary } from 'app/entities/horary/horary.model';
import { HoraryService } from 'app/entities/horary/service/horary.service';

import { WorkDayUpdateComponent } from './work-day-update.component';

describe('WorkDay Management Update Component', () => {
  let comp: WorkDayUpdateComponent;
  let fixture: ComponentFixture<WorkDayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workDayService: WorkDayService;
  let horaryService: HoraryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WorkDayUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WorkDayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkDayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workDayService = TestBed.inject(WorkDayService);
    horaryService = TestBed.inject(HoraryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Horary query and add missing value', () => {
      const workDay: IWorkDay = { id: 456 };
      const horary: IHorary = { id: 72262 };
      workDay.horary = horary;

      const horaryCollection: IHorary[] = [{ id: 23005 }];
      jest.spyOn(horaryService, 'query').mockReturnValue(of(new HttpResponse({ body: horaryCollection })));
      const additionalHoraries = [horary];
      const expectedCollection: IHorary[] = [...additionalHoraries, ...horaryCollection];
      jest.spyOn(horaryService, 'addHoraryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workDay });
      comp.ngOnInit();

      expect(horaryService.query).toHaveBeenCalled();
      expect(horaryService.addHoraryToCollectionIfMissing).toHaveBeenCalledWith(horaryCollection, ...additionalHoraries);
      expect(comp.horariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workDay: IWorkDay = { id: 456 };
      const horary: IHorary = { id: 98962 };
      workDay.horary = horary;

      activatedRoute.data = of({ workDay });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(workDay));
      expect(comp.horariesSharedCollection).toContain(horary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkDay>>();
      const workDay = { id: 123 };
      jest.spyOn(workDayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workDay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workDay }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(workDayService.update).toHaveBeenCalledWith(workDay);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkDay>>();
      const workDay = new WorkDay();
      jest.spyOn(workDayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workDay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workDay }));
      saveSubject.complete();

      // THEN
      expect(workDayService.create).toHaveBeenCalledWith(workDay);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkDay>>();
      const workDay = { id: 123 };
      jest.spyOn(workDayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workDay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workDayService.update).toHaveBeenCalledWith(workDay);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackHoraryById', () => {
      it('Should return tracked Horary primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHoraryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
