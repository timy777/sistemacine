import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersona, Persona } from '../persona.model';
import { PersonaService } from '../service/persona.service';
import { TipoPersona } from 'app/entities/enumerations/tipo-persona.model';
import { Sexo } from 'app/entities/enumerations/sexo.model';

@Component({
  selector: 'jhi-persona-update',
  templateUrl: './persona-update.component.html',
})
export class PersonaUpdateComponent implements OnInit {
  isSaving = false;
  tipoPersonaValues = Object.keys(TipoPersona);
  sexoValues = Object.keys(Sexo);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    apellido: [null, [Validators.required]],
    telefono: [],
    email: [null, [Validators.required]],
    tipo: [null, [Validators.required]],
    fechaNacimiento: [null, [Validators.required]],
    sexo: [null, [Validators.required]],
    carnetIdentidad: [null, [Validators.required]],
  });

  constructor(protected personaService: PersonaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ persona }) => {
      this.updateForm(persona);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const persona = this.createFromForm();
    if (persona.id !== undefined) {
      this.subscribeToSaveResponse(this.personaService.update(persona));
    } else {
      this.subscribeToSaveResponse(this.personaService.create(persona));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersona>>): void {
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

  protected updateForm(persona: IPersona): void {
    this.editForm.patchValue({
      id: persona.id,
      nombre: persona.nombre,
      apellido: persona.apellido,
      telefono: persona.telefono,
      email: persona.email,
      tipo: persona.tipo,
      fechaNacimiento: persona.fechaNacimiento,
      sexo: persona.sexo,
      carnetIdentidad: persona.carnetIdentidad,
    });
  }

  protected createFromForm(): IPersona {
    return {
      ...new Persona(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      email: this.editForm.get(['email'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      carnetIdentidad: this.editForm.get(['carnetIdentidad'])!.value,
    };
  }
}
