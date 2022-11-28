import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceProduct, getInvoiceProductIdentifier } from '../invoice-product.model';

export type EntityResponseType = HttpResponse<IInvoiceProduct>;
export type EntityArrayResponseType = HttpResponse<IInvoiceProduct[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceProduct: IInvoiceProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProduct);
    return this.http
      .post<IInvoiceProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoiceProduct: IInvoiceProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProduct);
    return this.http
      .put<IInvoiceProduct>(`${this.resourceUrl}/${getInvoiceProductIdentifier(invoiceProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(invoiceProduct: IInvoiceProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProduct);
    return this.http
      .patch<IInvoiceProduct>(`${this.resourceUrl}/${getInvoiceProductIdentifier(invoiceProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoiceProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoiceProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceProductToCollectionIfMissing(
    invoiceProductCollection: IInvoiceProduct[],
    ...invoiceProductsToCheck: (IInvoiceProduct | null | undefined)[]
  ): IInvoiceProduct[] {
    const invoiceProducts: IInvoiceProduct[] = invoiceProductsToCheck.filter(isPresent);
    if (invoiceProducts.length > 0) {
      const invoiceProductCollectionIdentifiers = invoiceProductCollection.map(
        invoiceProductItem => getInvoiceProductIdentifier(invoiceProductItem)!
      );
      const invoiceProductsToAdd = invoiceProducts.filter(invoiceProductItem => {
        const invoiceProductIdentifier = getInvoiceProductIdentifier(invoiceProductItem);
        if (invoiceProductIdentifier == null || invoiceProductCollectionIdentifiers.includes(invoiceProductIdentifier)) {
          return false;
        }
        invoiceProductCollectionIdentifiers.push(invoiceProductIdentifier);
        return true;
      });
      return [...invoiceProductsToAdd, ...invoiceProductCollection];
    }
    return invoiceProductCollection;
  }

  protected convertDateFromClient(invoiceProduct: IInvoiceProduct): IInvoiceProduct {
    return Object.assign({}, invoiceProduct, {
      date: invoiceProduct.date?.isValid() ? invoiceProduct.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoiceProduct: IInvoiceProduct) => {
        invoiceProduct.date = invoiceProduct.date ? dayjs(invoiceProduct.date) : undefined;
      });
    }
    return res;
  }
}
