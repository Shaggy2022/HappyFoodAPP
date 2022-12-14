import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-type',
        data: { pageTitle: 'happyFoodApp.documentType.home.title' },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'happyFoodApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'happyFoodApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'happyFoodApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'invoice-product',
        data: { pageTitle: 'happyFoodApp.invoiceProduct.home.title' },
        loadChildren: () => import('./invoice-product/invoice-product.module').then(m => m.InvoiceProductModule),
      },
      {
        path: 'work-day',
        data: { pageTitle: 'happyFoodApp.workDay.home.title' },
        loadChildren: () => import('./work-day/work-day.module').then(m => m.WorkDayModule),
      },
      {
        path: 'horary',
        data: { pageTitle: 'happyFoodApp.horary.home.title' },
        loadChildren: () => import('./horary/horary.module').then(m => m.HoraryModule),
      },
      {
        path: 'manager',
        data: { pageTitle: 'happyFoodApp.manager.home.title' },
        loadChildren: () => import('./manager/manager.module').then(m => m.ManagerModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'happyFoodApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'happyFoodApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'inventory',
        data: { pageTitle: 'happyFoodApp.inventory.home.title' },
        loadChildren: () => import('./inventory/inventory.module').then(m => m.InventoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
