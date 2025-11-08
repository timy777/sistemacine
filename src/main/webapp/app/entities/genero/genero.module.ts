import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GeneroComponent } from './list/genero.component';
import { GeneroDetailComponent } from './detail/genero-detail.component';
import { GeneroUpdateComponent } from './update/genero-update.component';
import { GeneroDeleteDialogComponent } from './delete/genero-delete-dialog.component';
import { GeneroRoutingModule } from './route/genero-routing.module';

@NgModule({
  imports: [SharedModule, GeneroRoutingModule],
  declarations: [GeneroComponent, GeneroDetailComponent, GeneroUpdateComponent, GeneroDeleteDialogComponent],
  entryComponents: [GeneroDeleteDialogComponent],
})
export class GeneroModule {}
