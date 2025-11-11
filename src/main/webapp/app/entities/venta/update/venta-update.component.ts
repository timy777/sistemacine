import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;

  personasSharedCollection: IPersona[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    total: [null, [Validators.required]],
    metodoPago: [null, [Validators.required]],
    cliente: [],
    vendedor: [],
  });

  constructor(
    protected ventaService: VentaService,
    protected personaService: PersonaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      if (venta.id === undefined) {
        const today = dayjs().startOf('day');
        venta.fecha = today;
      }

      this.updateForm(venta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  trackPersonaById(_index: number, item: IPersona): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
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

  protected updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      fecha: venta.fecha ? venta.fecha.format(DATE_TIME_FORMAT) : null,
      total: venta.total,
      metodoPago: venta.metodoPago,
      cliente: venta.cliente,
      vendedor: venta.vendedor,
    });

    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(
      this.personasSharedCollection,
      venta.cliente,
      venta.vendedor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) =>
          this.personaService.addPersonaToCollectionIfMissing(
            personas,
            this.editForm.get('cliente')!.value,
            this.editForm.get('vendedor')!.value
          )
        )
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
  }

  protected createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      total: this.editForm.get(['total'])!.value,
      metodoPago: this.editForm.get(['metodoPago'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      vendedor: this.editForm.get(['vendedor'])!.value,
    };
  }
}
