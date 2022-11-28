import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceProduct, InvoiceProduct } from '../invoice-product.model';
import { InvoiceProductService } from '../service/invoice-product.service';

@Injectable({ providedIn: 'root' })
export class InvoiceProductRoutingResolveService implements Resolve<IInvoiceProduct> {
  constructor(protected service: InvoiceProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoiceProduct: HttpResponse<InvoiceProduct>) => {
          if (invoiceProduct.body) {
            return of(invoiceProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvoiceProduct());
  }
}
