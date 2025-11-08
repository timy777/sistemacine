import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TarifaComponent } from './list/tarifa.component';
import { TarifaDetailComponent } from './detail/tarifa-detail.component';
import { TarifaUpdateComponent } from './update/tarifa-update.component';
import { TarifaDeleteDialogComponent } from './delete/tarifa-delete-dialog.component';
import { TarifaRoutingModule } from './route/tarifa-routing.module';

@NgModule({
  imports: [SharedModule, TarifaRoutingModule],
  declarations: [TarifaComponent, TarifaDetailComponent, TarifaUpdateComponent, TarifaDeleteDialogComponent],
  entryComponents: [TarifaDeleteDialogComponent],
})
export class TarifaModule {}
