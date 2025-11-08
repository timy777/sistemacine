import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';

@Component({
  templateUrl: './promocion-delete-dialog.component.html',
})
export class PromocionDeleteDialogComponent {
  promocion?: IPromocion;

  constructor(protected promocionService: PromocionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promocionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
