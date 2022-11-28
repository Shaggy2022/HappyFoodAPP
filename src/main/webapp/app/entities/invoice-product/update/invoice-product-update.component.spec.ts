import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceProductService } from '../service/invoice-product.service';
import { IInvoiceProduct, InvoiceProduct } from '../invoice-product.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

import { InvoiceProductUpdateComponent } from './invoice-product-update.component';

describe('InvoiceProduct Management Update Component', () => {
  let comp: InvoiceProductUpdateComponent;
  let fixture: ComponentFixture<InvoiceProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceProductService: InvoiceProductService;
  let productService: ProductService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvoiceProductUpdateComponent],
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
      .overrideTemplate(InvoiceProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceProductService = TestBed.inject(InvoiceProductService);
    productService = TestBed.inject(ProductService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const invoiceProduct: IInvoiceProduct = { id: 456 };
      const product: IProduct = { id: 80065 };
      invoiceProduct.product = product;

      const productCollection: IProduct[] = [{ id: 90490 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Invoice query and add missing value', () => {
      const invoiceProduct: IInvoiceProduct = { id: 456 };
      const invoice: IInvoice = { id: 47833 };
      invoiceProduct.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 10762 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoices);
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceProduct: IInvoiceProduct = { id: 456 };
      const product: IProduct = { id: 34640 };
      invoiceProduct.product = product;
      const invoice: IInvoice = { id: 27227 };
      invoiceProduct.invoice = invoice;

      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(invoiceProduct));
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.invoicesSharedCollection).toContain(invoice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceProduct>>();
      const invoiceProduct = { id: 123 };
      jest.spyOn(invoiceProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceProductService.update).toHaveBeenCalledWith(invoiceProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceProduct>>();
      const invoiceProduct = new InvoiceProduct();
      jest.spyOn(invoiceProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceProduct }));
      saveSubject.complete();

      // THEN
      expect(invoiceProductService.create).toHaveBeenCalledWith(invoiceProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceProduct>>();
      const invoiceProduct = { id: 123 };
      jest.spyOn(invoiceProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceProductService.update).toHaveBeenCalledWith(invoiceProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInvoiceById', () => {
      it('Should return tracked Invoice primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInvoiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
