import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetalleVentaComponent } from './list/detalle-venta.component';
import { DetalleVentaDetailComponent } from './detail/detalle-venta-detail.component';
import { DetalleVentaUpdateComponent } from './update/detalle-venta-update.component';
import { DetalleVentaDeleteDialogComponent } from './delete/detalle-venta-delete-dialog.component';
import { DetalleVentaRoutingModule } from './route/detalle-venta-routing.module';

@NgModule({
  imports: [SharedModule, DetalleVentaRoutingModule],
  declarations: [DetalleVentaComponent, DetalleVentaDetailComponent, DetalleVentaUpdateComponent, DetalleVentaDeleteDialogComponent],
  entryComponents: [DetalleVentaDeleteDialogComponent],
})
export class DetalleVentaModule {}
