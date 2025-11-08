import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPelicula, Pelicula } from '../pelicula.model';
import { PeliculaService } from '../service/pelicula.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IGenero } from 'app/entities/genero/genero.model';
import { GeneroService } from 'app/entities/genero/service/genero.service';
import { EstadoPelicula } from 'app/entities/enumerations/estado-pelicula.model';

@Component({
  selector: 'jhi-pelicula-update',
  templateUrl: './pelicula-update.component.html',
})
export class PeliculaUpdateComponent implements OnInit {
  isSaving = false;
  estadoPeliculaValues = Object.keys(EstadoPelicula);

  generosSharedCollection: IGenero[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [null, [Validators.required]],
    sinopsis: [],
    duracion: [null, [Validators.required]],
    idioma: [],
    clasificacion: [],
    formato: [],
    estado: [null, [Validators.required]],
    imagenUrl: [],
    genero: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected peliculaService: PeliculaService,
    protected generoService: GeneroService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pelicula }) => {
      this.updateForm(pelicula);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('sistemacineApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pelicula = this.createFromForm();
    if (pelicula.id !== undefined) {
      this.subscribeToSaveResponse(this.peliculaService.update(pelicula));
    } else {
      this.subscribeToSaveResponse(this.peliculaService.create(pelicula));
    }
  }

  trackGeneroById(_index: number, item: IGenero): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPelicula>>): void {
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

  protected updateForm(pelicula: IPelicula): void {
    this.editForm.patchValue({
      id: pelicula.id,
      titulo: pelicula.titulo,
      sinopsis: pelicula.sinopsis,
      duracion: pelicula.duracion,
      idioma: pelicula.idioma,
      clasificacion: pelicula.clasificacion,
      formato: pelicula.formato,
      estado: pelicula.estado,
      imagenUrl: pelicula.imagenUrl,
      genero: pelicula.genero,
    });

    this.generosSharedCollection = this.generoService.addGeneroToCollectionIfMissing(this.generosSharedCollection, pelicula.genero);
  }

  protected loadRelationshipsOptions(): void {
    this.generoService
      .query()
      .pipe(map((res: HttpResponse<IGenero[]>) => res.body ?? []))
      .pipe(map((generos: IGenero[]) => this.generoService.addGeneroToCollectionIfMissing(generos, this.editForm.get('genero')!.value)))
      .subscribe((generos: IGenero[]) => (this.generosSharedCollection = generos));
  }

  protected createFromForm(): IPelicula {
    return {
      ...new Pelicula(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      sinopsis: this.editForm.get(['sinopsis'])!.value,
      duracion: this.editForm.get(['duracion'])!.value,
      idioma: this.editForm.get(['idioma'])!.value,
      clasificacion: this.editForm.get(['clasificacion'])!.value,
      formato: this.editForm.get(['formato'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      imagenUrl: this.editForm.get(['imagenUrl'])!.value,
      genero: this.editForm.get(['genero'])!.value,
    };
  }
}
