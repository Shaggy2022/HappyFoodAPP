import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInvoiceProduct, InvoiceProduct } from '../invoice-product.model';
import { InvoiceProductService } from '../service/invoice-product.service';

import { InvoiceProductRoutingResolveService } from './invoice-product-routing-resolve.service';

describe('InvoiceProduct routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InvoiceProductRoutingResolveService;
  let service: InvoiceProductService;
  let resultInvoiceProduct: IInvoiceProduct | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InvoiceProductRoutingResolveService);
    service = TestBed.inject(InvoiceProductService);
    resultInvoiceProduct = undefined;
  });

  describe('resolve', () => {
    it('should return IInvoiceProduct returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceProduct).toEqual({ id: 123 });
    });

    it('should return new IInvoiceProduct if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceProduct = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInvoiceProduct).toEqual(new InvoiceProduct());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvoiceProduct })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceProduct).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
