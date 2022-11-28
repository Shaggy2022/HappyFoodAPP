import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceProductDetailComponent } from './invoice-product-detail.component';

describe('InvoiceProduct Management Detail Component', () => {
  let comp: InvoiceProductDetailComponent;
  let fixture: ComponentFixture<InvoiceProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvoiceProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ invoiceProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvoiceProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvoiceProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load invoiceProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.invoiceProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
