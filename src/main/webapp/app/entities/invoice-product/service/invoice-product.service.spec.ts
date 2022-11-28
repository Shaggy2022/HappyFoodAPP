import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInvoiceProduct, InvoiceProduct } from '../invoice-product.model';

import { InvoiceProductService } from './invoice-product.service';

describe('InvoiceProduct Service', () => {
  let service: InvoiceProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvoiceProduct;
  let expectedResult: IInvoiceProduct | IInvoiceProduct[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceProductService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InvoiceProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new InvoiceProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceProduct', () => {
      const patchObject = Object.assign({}, new InvoiceProduct());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InvoiceProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvoiceProductToCollectionIfMissing', () => {
      it('should add a InvoiceProduct to an empty array', () => {
        const invoiceProduct: IInvoiceProduct = { id: 123 };
        expectedResult = service.addInvoiceProductToCollectionIfMissing([], invoiceProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceProduct);
      });

      it('should not add a InvoiceProduct to an array that contains it', () => {
        const invoiceProduct: IInvoiceProduct = { id: 123 };
        const invoiceProductCollection: IInvoiceProduct[] = [
          {
            ...invoiceProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvoiceProductToCollectionIfMissing(invoiceProductCollection, invoiceProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceProduct to an array that doesn't contain it", () => {
        const invoiceProduct: IInvoiceProduct = { id: 123 };
        const invoiceProductCollection: IInvoiceProduct[] = [{ id: 456 }];
        expectedResult = service.addInvoiceProductToCollectionIfMissing(invoiceProductCollection, invoiceProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceProduct);
      });

      it('should add only unique InvoiceProduct to an array', () => {
        const invoiceProductArray: IInvoiceProduct[] = [{ id: 123 }, { id: 456 }, { id: 59120 }];
        const invoiceProductCollection: IInvoiceProduct[] = [{ id: 123 }];
        expectedResult = service.addInvoiceProductToCollectionIfMissing(invoiceProductCollection, ...invoiceProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceProduct: IInvoiceProduct = { id: 123 };
        const invoiceProduct2: IInvoiceProduct = { id: 456 };
        expectedResult = service.addInvoiceProductToCollectionIfMissing([], invoiceProduct, invoiceProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceProduct);
        expect(expectedResult).toContain(invoiceProduct2);
      });

      it('should accept null and undefined values', () => {
        const invoiceProduct: IInvoiceProduct = { id: 123 };
        expectedResult = service.addInvoiceProductToCollectionIfMissing([], null, invoiceProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceProduct);
      });

      it('should return initial array if no InvoiceProduct is added', () => {
        const invoiceProductCollection: IInvoiceProduct[] = [{ id: 123 }];
        expectedResult = service.addInvoiceProductToCollectionIfMissing(invoiceProductCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
