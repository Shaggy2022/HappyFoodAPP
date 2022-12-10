import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Authority } from '../config/authority.constants';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-type',
        data: {
          pageTitle: 'happyFoodApp.documentType.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER],
        },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      {
        path: 'customer',
        data: {
          pageTitle: 'happyFoodApp.customer.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE, Authority.CUSTOMER],
        },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'invoice',
        data: {
          pageTitle: 'happyFoodApp.invoice.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE, Authority.CUSTOMER],
        },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'product',
        data: {
          pageTitle: 'happyFoodApp.product.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE, Authority.CUSTOMER],
        },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'invoice-product',
        data: {
          pageTitle: 'happyFoodApp.invoiceProduct.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER],
        },
        loadChildren: () => import('./invoice-product/invoice-product.module').then(m => m.InvoiceProductModule),
      },
      {
        path: 'work-day',
        data: {
          pageTitle: 'happyFoodApp.workDay.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
        },
        loadChildren: () => import('./work-day/work-day.module').then(m => m.WorkDayModule),
      },
      {
        path: 'horary',
        data: {
          pageTitle: 'happyFoodApp.horary.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER],
        },
        loadChildren: () => import('./horary/horary.module').then(m => m.HoraryModule),
      },
      {
        path: 'manager',
        data: {
          pageTitle: 'happyFoodApp.manager.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
        },
        loadChildren: () => import('./manager/manager.module').then(m => m.ManagerModule),
      },
      {
        path: 'employee',
        data: {
          pageTitle: 'happyFoodApp.employee.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
        },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'order',
        data: {
          pageTitle: 'happyFoodApp.order.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE, Authority.CUSTOMER],
        },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'inventory',
        data: {
          pageTitle: 'happyFoodApp.inventory.home.title',
          authorities: [Authority.ADMIN, Authority.MANAGER, Authority.EMPLOYEE],
        },
        loadChildren: () => import('./inventory/inventory.module').then(m => m.InventoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
