import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPromocion, Promocion } from '../promocion.model';
import { PromocionService } from '../service/promocion.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';

@Component({
  selector: 'jhi-promocion-update',
  templateUrl: './promocion-update.component.html',
})
export class PromocionUpdateComponent implements OnInit {
  isSaving = false;

  peliculasSharedCollection: IPelicula[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [],
    porcentajeDescuento: [null, [Validators.required]],
    fechaInicio: [null, [Validators.required]],
    fechaFin: [null, [Validators.required]],
    activa: [null, [Validators.required]],
    peliculas: [],
  });

  constructor(
    protected promocionService: PromocionService,
    protected peliculaService: PeliculaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promocion }) => {
      this.updateForm(promocion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promocion = this.createFromForm();
    if (promocion.id !== undefined) {
      this.subscribeToSaveResponse(this.promocionService.update(promocion));
    } else {
      this.subscribeToSaveResponse(this.promocionService.create(promocion));
    }
  }

  trackPeliculaById(_index: number, item: IPelicula): number {
    return item.id!;
  }

  getSelectedPelicula(option: IPelicula, selectedVals?: IPelicula[]): IPelicula {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromocion>>): void {
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

  protected updateForm(promocion: IPromocion): void {
    this.editForm.patchValue({
      id: promocion.id,
      nombre: promocion.nombre,
      descripcion: promocion.descripcion,
      porcentajeDescuento: promocion.porcentajeDescuento,
      fechaInicio: promocion.fechaInicio,
      fechaFin: promocion.fechaFin,
      activa: promocion.activa,
      peliculas: promocion.peliculas,
    });

    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(
      this.peliculasSharedCollection,
      ...(promocion.peliculas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, ...(this.editForm.get('peliculas')!.value ?? []))
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));
  }

  protected createFromForm(): IPromocion {
    return {
      ...new Promocion(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      porcentajeDescuento: this.editForm.get(['porcentajeDescuento'])!.value,
      fechaInicio: this.editForm.get(['fechaInicio'])!.value,
      fechaFin: this.editForm.get(['fechaFin'])!.value,
      activa: this.editForm.get(['activa'])!.value,
      peliculas: this.editForm.get(['peliculas'])!.value,
    };
  }
}
