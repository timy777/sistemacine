import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISala, Sala } from '../sala.model';
import { SalaService } from '../service/sala.service';
import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';

@Component({
  selector: 'jhi-sala-update',
  templateUrl: './sala-update.component.html',
})
export class SalaUpdateComponent implements OnInit {
  isSaving = false;
  tipoSalaValues = Object.keys(TipoSala);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    capacidad: [null, [Validators.required]],
    tipo: [null, [Validators.required]],
    estado: [],
  });

  constructor(protected salaService: SalaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sala }) => {
      this.updateForm(sala);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sala = this.createFromForm();
    if (sala.id !== undefined) {
      this.subscribeToSaveResponse(this.salaService.update(sala));
    } else {
      this.subscribeToSaveResponse(this.salaService.create(sala));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISala>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sala: ISala): void {
    this.editForm.patchValue({
      id: sala.id,
      nombre: sala.nombre,
      capacidad: sala.capacidad,
      tipo: sala.tipo,
      estado: sala.estado,
    });
  }

  protected createFromForm(): ISala {
    return {
      ...new Sala(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      capacidad: this.editForm.get(['capacidad'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
