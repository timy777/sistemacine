import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITarifa } from '../tarifa.model';
import { TarifaService } from '../service/tarifa.service';

@Component({
  templateUrl: './tarifa-delete-dialog.component.html',
})
export class TarifaDeleteDialogComponent {
  tarifa?: ITarifa;

  constructor(protected tarifaService: TarifaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tarifaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
