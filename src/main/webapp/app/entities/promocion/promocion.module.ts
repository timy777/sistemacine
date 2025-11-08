import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PromocionComponent } from './list/promocion.component';
import { PromocionDetailComponent } from './detail/promocion-detail.component';
import { PromocionUpdateComponent } from './update/promocion-update.component';
import { PromocionDeleteDialogComponent } from './delete/promocion-delete-dialog.component';
import { PromocionRoutingModule } from './route/promocion-routing.module';

@NgModule({
  imports: [SharedModule, PromocionRoutingModule],
  declarations: [PromocionComponent, PromocionDetailComponent, PromocionUpdateComponent, PromocionDeleteDialogComponent],
  entryComponents: [PromocionDeleteDialogComponent],
})
export class PromocionModule {}
