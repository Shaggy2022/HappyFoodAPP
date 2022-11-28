import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HoraryService } from '../service/horary.service';
import { IHorary, Horary } from '../horary.model';

import { HoraryUpdateComponent } from './horary-update.component';

describe('Horary Management Update Component', () => {
  let comp: HoraryUpdateComponent;
  let fixture: ComponentFixture<HoraryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let horaryService: HoraryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HoraryUpdateComponent],
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
      .overrideTemplate(HoraryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HoraryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    horaryService = TestBed.inject(HoraryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const horary: IHorary = { id: 456 };

      activatedRoute.data = of({ horary });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(horary));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horary>>();
      const horary = { id: 123 };
      jest.spyOn(horaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horary }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(horaryService.update).toHaveBeenCalledWith(horary);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horary>>();
      const horary = new Horary();
      jest.spyOn(horaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horary }));
      saveSubject.complete();

      // THEN
      expect(horaryService.create).toHaveBeenCalledWith(horary);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horary>>();
      const horary = { id: 123 };
      jest.spyOn(horaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(horaryService.update).toHaveBeenCalledWith(horary);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
