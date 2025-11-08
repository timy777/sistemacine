import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromocionService } from '../service/promocion.service';
import { IPromocion, Promocion } from '../promocion.model';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';

import { PromocionUpdateComponent } from './promocion-update.component';

describe('Promocion Management Update Component', () => {
  let comp: PromocionUpdateComponent;
  let fixture: ComponentFixture<PromocionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promocionService: PromocionService;
  let peliculaService: PeliculaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromocionUpdateComponent],
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
      .overrideTemplate(PromocionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromocionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promocionService = TestBed.inject(PromocionService);
    peliculaService = TestBed.inject(PeliculaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pelicula query and add missing value', () => {
      const promocion: IPromocion = { id: 456 };
      const peliculas: IPelicula[] = [{ id: 23334 }];
      promocion.peliculas = peliculas;

      const peliculaCollection: IPelicula[] = [{ id: 27889 }];
      jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
      const additionalPeliculas = [...peliculas];
      const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
      jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(peliculaService.query).toHaveBeenCalled();
      expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
      expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const promocion: IPromocion = { id: 456 };
      const peliculas: IPelicula = { id: 83821 };
      promocion.peliculas = [peliculas];

      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(promocion));
      expect(comp.peliculasSharedCollection).toContain(peliculas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = { id: 123 };
      jest.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promocion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(promocionService.update).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = new Promocion();
      jest.spyOn(promocionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promocion }));
      saveSubject.complete();

      // THEN
      expect(promocionService.create).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Promocion>>();
      const promocion = { id: 123 };
      jest.spyOn(promocionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promocion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promocionService.update).toHaveBeenCalledWith(promocion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPeliculaById', () => {
      it('Should return tracked Pelicula primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeliculaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPelicula', () => {
      it('Should return option if no Pelicula is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPelicula(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Pelicula for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPelicula(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Pelicula is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPelicula(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
