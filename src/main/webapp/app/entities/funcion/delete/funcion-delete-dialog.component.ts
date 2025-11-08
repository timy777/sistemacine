import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncion } from '../funcion.model';
import { FuncionService } from '../service/funcion.service';

@Component({
  templateUrl: './funcion-delete-dialog.component.html',
})
export class FuncionDeleteDialogComponent {
  funcion?: IFuncion;

  constructor(protected funcionService: FuncionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
