import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GeneroService } from '../service/genero.service';
import { IGenero, Genero } from '../genero.model';

import { GeneroUpdateComponent } from './genero-update.component';

describe('Genero Management Update Component', () => {
  let comp: GeneroUpdateComponent;
  let fixture: ComponentFixture<GeneroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let generoService: GeneroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GeneroUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GeneroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GeneroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    generoService = TestBed.inject(GeneroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const genero: IGenero = { id: 456 };

      activatedRoute.data = of({ genero });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(genero));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genero>>();
      const genero = { id: 123 };
      jest.spyOn(generoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genero });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genero }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(generoService.update).toHaveBeenCalledWith(genero);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genero>>();
      const genero = new Genero();
      jest.spyOn(generoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genero });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genero }));
      saveSubject.complete();

      // THEN
      expect(generoService.create).toHaveBeenCalledWith(genero);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genero>>();
      const genero = { id: 123 };
      jest.spyOn(generoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genero });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(generoService.update).toHaveBeenCalledWith(genero);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
