import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetalleVenta, DetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';
import { IFuncion } from 'app/entities/funcion/funcion.model';
import { FuncionService } from 'app/entities/funcion/service/funcion.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-detalle-venta-update',
  templateUrl: './detalle-venta-update.component.html',
})
export class DetalleVentaUpdateComponent implements OnInit {
  isSaving = false;

  funcionsSharedCollection: IFuncion[] = [];
  ventasSharedCollection: IVenta[] = [];

  editForm = this.fb.group({
    id: [],
    asiento: [null, [Validators.required]],
    precioUnitario: [null, [Validators.required]],
    funcion: [],
    venta: [],
  });

  constructor(
    protected detalleVentaService: DetalleVentaService,
    protected funcionService: FuncionService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleVenta }) => {
      this.updateForm(detalleVenta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleVenta = this.createFromForm();
    if (detalleVenta.id !== undefined) {
      this.subscribeToSaveResponse(this.detalleVentaService.update(detalleVenta));
    } else {
      this.subscribeToSaveResponse(this.detalleVentaService.create(detalleVenta));
    }
  }

  trackFuncionById(_index: number, item: IFuncion): number {
    return item.id!;
  }

  trackVentaById(_index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleVenta>>): void {
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

  protected updateForm(detalleVenta: IDetalleVenta): void {
    this.editForm.patchValue({
      id: detalleVenta.id,
      asiento: detalleVenta.asiento,
      precioUnitario: detalleVenta.precioUnitario,
      funcion: detalleVenta.funcion,
      venta: detalleVenta.venta,
    });

    this.funcionsSharedCollection = this.funcionService.addFuncionToCollectionIfMissing(
      this.funcionsSharedCollection,
      detalleVenta.funcion
    );
    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, detalleVenta.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.funcionService
      .query()
      .pipe(map((res: HttpResponse<IFuncion[]>) => res.body ?? []))
      .pipe(
        map((funcions: IFuncion[]) => this.funcionService.addFuncionToCollectionIfMissing(funcions, this.editForm.get('funcion')!.value))
      )
      .subscribe((funcions: IFuncion[]) => (this.funcionsSharedCollection = funcions));

    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): IDetalleVenta {
    return {
      ...new DetalleVenta(),
      id: this.editForm.get(['id'])!.value,
      asiento: this.editForm.get(['asiento'])!.value,
      precioUnitario: this.editForm.get(['precioUnitario'])!.value,
      funcion: this.editForm.get(['funcion'])!.value,
      venta: this.editForm.get(['venta'])!.value,
    };
  }
}
