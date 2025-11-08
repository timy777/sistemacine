import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGenero, Genero } from '../genero.model';
import { GeneroService } from '../service/genero.service';

@Component({
  selector: 'jhi-genero-update',
  templateUrl: './genero-update.component.html',
})
export class GeneroUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    descripcion: [],
  });

  constructor(protected generoService: GeneroService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ genero }) => {
      this.updateForm(genero);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const genero = this.createFromForm();
    if (genero.id !== undefined) {
      this.subscribeToSaveResponse(this.generoService.update(genero));
    } else {
      this.subscribeToSaveResponse(this.generoService.create(genero));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGenero>>): void {
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

  protected updateForm(genero: IGenero): void {
    this.editForm.patchValue({
      id: genero.id,
      nombre: genero.nombre,
      descripcion: genero.descripcion,
    });
  }

  protected createFromForm(): IGenero {
    return {
      ...new Genero(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
