import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PeliculaService } from '../service/pelicula.service';
import { IPelicula, Pelicula } from '../pelicula.model';
import { IGenero } from 'app/entities/genero/genero.model';
import { GeneroService } from 'app/entities/genero/service/genero.service';

import { PeliculaUpdateComponent } from './pelicula-update.component';

describe('Pelicula Management Update Component', () => {
  let comp: PeliculaUpdateComponent;
  let fixture: ComponentFixture<PeliculaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peliculaService: PeliculaService;
  let generoService: GeneroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PeliculaUpdateComponent],
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
      .overrideTemplate(PeliculaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeliculaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peliculaService = TestBed.inject(PeliculaService);
    generoService = TestBed.inject(GeneroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Genero query and add missing value', () => {
      const pelicula: IPelicula = { id: 'CBA' };
      const genero: IGenero = { id: '16e4d391-a3b9-4f8f-8c55-81822889d94d' };
      pelicula.genero = genero;

      const generoCollection: IGenero[] = [{ id: '670a35a3-432b-46da-a901-cd12743386fb' }];
      jest.spyOn(generoService, 'query').mockReturnValue(of(new HttpResponse({ body: generoCollection })));
      const additionalGeneros = [genero];
      const expectedCollection: IGenero[] = [...additionalGeneros, ...generoCollection];
      jest.spyOn(generoService, 'addGeneroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pelicula });
      comp.ngOnInit();

      expect(generoService.query).toHaveBeenCalled();
      expect(generoService.addGeneroToCollectionIfMissing).toHaveBeenCalledWith(generoCollection, ...additionalGeneros);
      expect(comp.generosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pelicula: IPelicula = { id: 'CBA' };
      const genero: IGenero = { id: '7b6d9ee2-4288-40ee-a498-d8d2612d6f2d' };
      pelicula.genero = genero;

      activatedRoute.data = of({ pelicula });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pelicula));
      expect(comp.generosSharedCollection).toContain(genero);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pelicula>>();
      const pelicula = { id: 'ABC' };
      jest.spyOn(peliculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pelicula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pelicula }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pelicula>>();
      const pelicula = new Pelicula();
      jest.spyOn(peliculaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pelicula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pelicula }));
      saveSubject.complete();

      // THEN
      expect(peliculaService.create).toHaveBeenCalledWith(pelicula);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pelicula>>();
      const pelicula = { id: 'ABC' };
      jest.spyOn(peliculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pelicula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peliculaService.update).toHaveBeenCalledWith(pelicula);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGeneroById', () => {
      it('Should return tracked Genero primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackGeneroById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
