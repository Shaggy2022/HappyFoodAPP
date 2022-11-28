import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { State } from 'app/entities/enumerations/state.model';
import { IWorkDay, WorkDay } from '../work-day.model';

import { WorkDayService } from './work-day.service';

describe('WorkDay Service', () => {
  let service: WorkDayService;
  let httpMock: HttpTestingController;
  let elemDefault: IWorkDay;
  let expectedResult: IWorkDay | IWorkDay[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkDayService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      dayName: 'AAAAAAA',
      month: 'AAAAAAA',
      state: State.ACTIVE,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WorkDay', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WorkDay()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkDay', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dayName: 'BBBBBB',
          month: 'BBBBBB',
          state: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkDay', () => {
      const patchObject = Object.assign(
        {
          dayName: 'BBBBBB',
          month: 'BBBBBB',
        },
        new WorkDay()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkDay', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dayName: 'BBBBBB',
          month: 'BBBBBB',
          state: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WorkDay', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWorkDayToCollectionIfMissing', () => {
      it('should add a WorkDay to an empty array', () => {
        const workDay: IWorkDay = { id: 123 };
        expectedResult = service.addWorkDayToCollectionIfMissing([], workDay);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workDay);
      });

      it('should not add a WorkDay to an array that contains it', () => {
        const workDay: IWorkDay = { id: 123 };
        const workDayCollection: IWorkDay[] = [
          {
            ...workDay,
          },
          { id: 456 },
        ];
        expectedResult = service.addWorkDayToCollectionIfMissing(workDayCollection, workDay);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkDay to an array that doesn't contain it", () => {
        const workDay: IWorkDay = { id: 123 };
        const workDayCollection: IWorkDay[] = [{ id: 456 }];
        expectedResult = service.addWorkDayToCollectionIfMissing(workDayCollection, workDay);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workDay);
      });

      it('should add only unique WorkDay to an array', () => {
        const workDayArray: IWorkDay[] = [{ id: 123 }, { id: 456 }, { id: 75582 }];
        const workDayCollection: IWorkDay[] = [{ id: 123 }];
        expectedResult = service.addWorkDayToCollectionIfMissing(workDayCollection, ...workDayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workDay: IWorkDay = { id: 123 };
        const workDay2: IWorkDay = { id: 456 };
        expectedResult = service.addWorkDayToCollectionIfMissing([], workDay, workDay2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workDay);
        expect(expectedResult).toContain(workDay2);
      });

      it('should accept null and undefined values', () => {
        const workDay: IWorkDay = { id: 123 };
        expectedResult = service.addWorkDayToCollectionIfMissing([], null, workDay, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workDay);
      });

      it('should return initial array if no WorkDay is added', () => {
        const workDayCollection: IWorkDay[] = [{ id: 123 }];
        expectedResult = service.addWorkDayToCollectionIfMissing(workDayCollection, undefined, null);
        expect(expectedResult).toEqual(workDayCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
