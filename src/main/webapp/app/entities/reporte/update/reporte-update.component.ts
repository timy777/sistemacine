import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReporte, Reporte } from '../reporte.model';
import { ReporteService } from '../service/reporte.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

@Component({
  selector: 'jhi-reporte-update',
  templateUrl: './reporte-update.component.html',
})
export class ReporteUpdateComponent implements OnInit {
  isSaving = false;

  personasSharedCollection: IPersona[] = [];

  editForm = this.fb.group({
    id: [],
    tipo: [null, [Validators.required]],
    fechaGeneracion: [null, [Validators.required]],
    descripcion: [],
    vendedor: [],
  });

  constructor(
    protected reporteService: ReporteService,
    protected personaService: PersonaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reporte }) => {
      this.updateForm(reporte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reporte = this.createFromForm();
    if (reporte.id !== undefined) {
      this.subscribeToSaveResponse(this.reporteService.update(reporte));
    } else {
      this.subscribeToSaveResponse(this.reporteService.create(reporte));
    }
  }

  trackPersonaById(_index: number, item: IPersona): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReporte>>): void {
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

  protected updateForm(reporte: IReporte): void {
    this.editForm.patchValue({
      id: reporte.id,
      tipo: reporte.tipo,
      fechaGeneracion: reporte.fechaGeneracion,
      descripcion: reporte.descripcion,
      vendedor: reporte.vendedor,
    });

    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasSharedCollection, reporte.vendedor);
  }

  protected loadRelationshipsOptions(): void {
    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('vendedor')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
  }

  protected createFromForm(): IReporte {
    return {
      ...new Reporte(),
      id: this.editForm.get(['id'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      fechaGeneracion: this.editForm.get(['fechaGeneracion'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      vendedor: this.editForm.get(['vendedor'])!.value,
    };
  }
}
