import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITarifa, Tarifa } from '../tarifa.model';
import { TarifaService } from '../service/tarifa.service';
import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';

@Component({
  selector: 'jhi-tarifa-update',
  templateUrl: './tarifa-update.component.html',
})
export class TarifaUpdateComponent implements OnInit {
  isSaving = false;
  tipoSalaValues = Object.keys(TipoSala);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [],
    monto: [null, [Validators.required]],
    diaSemana: [],
    tipoSala: [],
  });

  constructor(protected tarifaService: TarifaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tarifa }) => {
      this.updateForm(tarifa);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tarifa = this.createFromForm();
    if (tarifa.id !== undefined) {
      this.subscribeToSaveResponse(this.tarifaService.update(tarifa));
    } else {
      this.subscribeToSaveResponse(this.tarifaService.create(tarifa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITarifa>>): void {
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

  protected updateForm(tarifa: ITarifa): void {
    this.editForm.patchValue({
      id: tarifa.id,
      nombre: tarifa.nombre,
      descripcion: tarifa.descripcion,
      monto: tarifa.monto,
      diaSemana: tarifa.diaSemana,
      tipoSala: tarifa.tipoSala,
    });
  }

  protected createFromForm(): ITarifa {
    return {
      ...new Tarifa(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      monto: this.editForm.get(['monto'])!.value,
      diaSemana: this.editForm.get(['diaSemana'])!.value,
      tipoSala: this.editForm.get(['tipoSala'])!.value,
    };
  }
}
