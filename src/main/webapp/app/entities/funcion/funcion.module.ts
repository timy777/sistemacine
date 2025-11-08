import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FuncionComponent } from './list/funcion.component';
import { FuncionDetailComponent } from './detail/funcion-detail.component';
import { FuncionUpdateComponent } from './update/funcion-update.component';
import { FuncionDeleteDialogComponent } from './delete/funcion-delete-dialog.component';
import { FuncionRoutingModule } from './route/funcion-routing.module';

@NgModule({
  imports: [SharedModule, FuncionRoutingModule],
  declarations: [FuncionComponent, FuncionDetailComponent, FuncionUpdateComponent, FuncionDeleteDialogComponent],
  entryComponents: [FuncionDeleteDialogComponent],
})
export class FuncionModule {}
