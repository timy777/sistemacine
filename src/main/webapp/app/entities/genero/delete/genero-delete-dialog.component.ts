import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGenero } from '../genero.model';
import { GeneroService } from '../service/genero.service';

@Component({
  templateUrl: './genero-delete-dialog.component.html',
})
export class GeneroDeleteDialogComponent {
  genero?: IGenero;

  constructor(protected generoService: GeneroService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.generoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
