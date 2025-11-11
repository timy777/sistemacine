import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFuncion, Funcion } from '../funcion.model';
import { FuncionService } from '../service/funcion.service';
import { ISala } from 'app/entities/sala/sala.model';
import { SalaService } from 'app/entities/sala/service/sala.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { TarifaService } from 'app/entities/tarifa/service/tarifa.service';

@Component({
  selector: 'jhi-funcion-update',
  templateUrl: './funcion-update.component.html',
})
export class FuncionUpdateComponent implements OnInit {
  isSaving = false;

  salasSharedCollection: ISala[] = [];
  peliculasSharedCollection: IPelicula[] = [];
  tarifasSharedCollection: ITarifa[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    horaInicio: [null, [Validators.required]],
    horaFin: [null, [Validators.required]],
    precio: [null, [Validators.required]],
    sala: [],
    pelicula: [],
    tarifa: [],
  });

  constructor(
    protected funcionService: FuncionService,
    protected salaService: SalaService,
    protected peliculaService: PeliculaService,
    protected tarifaService: TarifaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcion }) => {
      if (funcion.id === undefined) {
        const today = dayjs().startOf('day');
        funcion.horaInicio = today;
        funcion.horaFin = today;
      }

      this.updateForm(funcion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcion = this.createFromForm();
    if (funcion.id !== undefined) {
      this.subscribeToSaveResponse(this.funcionService.update(funcion));
    } else {
      this.subscribeToSaveResponse(this.funcionService.create(funcion));
    }
  }

  trackSalaById(_index: number, item: ISala): string {
    return item.id!;
  }

  trackPeliculaById(_index: number, item: IPelicula): string {
    return item.id!;
  }

  trackTarifaById(_index: number, item: ITarifa): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncion>>): void {
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

  protected updateForm(funcion: IFuncion): void {
    this.editForm.patchValue({
      id: funcion.id,
      fecha: funcion.fecha,
      horaInicio: funcion.horaInicio ? funcion.horaInicio.format(DATE_TIME_FORMAT) : null,
      horaFin: funcion.horaFin ? funcion.horaFin.format(DATE_TIME_FORMAT) : null,
      precio: funcion.precio,
      sala: funcion.sala,
      pelicula: funcion.pelicula,
      tarifa: funcion.tarifa,
    });

    this.salasSharedCollection = this.salaService.addSalaToCollectionIfMissing(this.salasSharedCollection, funcion.sala);
    this.peliculasSharedCollection = this.peliculaService.addPeliculaToCollectionIfMissing(
      this.peliculasSharedCollection,
      funcion.pelicula
    );
    this.tarifasSharedCollection = this.tarifaService.addTarifaToCollectionIfMissing(this.tarifasSharedCollection, funcion.tarifa);
  }

  protected loadRelationshipsOptions(): void {
    this.salaService
      .query()
      .pipe(map((res: HttpResponse<ISala[]>) => res.body ?? []))
      .pipe(map((salas: ISala[]) => this.salaService.addSalaToCollectionIfMissing(salas, this.editForm.get('sala')!.value)))
      .subscribe((salas: ISala[]) => (this.salasSharedCollection = salas));

    this.peliculaService
      .query()
      .pipe(map((res: HttpResponse<IPelicula[]>) => res.body ?? []))
      .pipe(
        map((peliculas: IPelicula[]) =>
          this.peliculaService.addPeliculaToCollectionIfMissing(peliculas, this.editForm.get('pelicula')!.value)
        )
      )
      .subscribe((peliculas: IPelicula[]) => (this.peliculasSharedCollection = peliculas));

    this.tarifaService
      .query()
      .pipe(map((res: HttpResponse<ITarifa[]>) => res.body ?? []))
      .pipe(map((tarifas: ITarifa[]) => this.tarifaService.addTarifaToCollectionIfMissing(tarifas, this.editForm.get('tarifa')!.value)))
      .subscribe((tarifas: ITarifa[]) => (this.tarifasSharedCollection = tarifas));
  }

  protected createFromForm(): IFuncion {
    return {
      ...new Funcion(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      horaInicio: this.editForm.get(['horaInicio'])!.value ? dayjs(this.editForm.get(['horaInicio'])!.value, DATE_TIME_FORMAT) : undefined,
      horaFin: this.editForm.get(['horaFin'])!.value ? dayjs(this.editForm.get(['horaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      precio: this.editForm.get(['precio'])!.value,
      sala: this.editForm.get(['sala'])!.value,
      pelicula: this.editForm.get(['pelicula'])!.value,
      tarifa: this.editForm.get(['tarifa'])!.value,
    };
  }
}
