import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FuncionService } from '../service/funcion.service';
import { IFuncion, Funcion } from '../funcion.model';
import { ISala } from 'app/entities/sala/sala.model';
import { SalaService } from 'app/entities/sala/service/sala.service';
import { IPelicula } from 'app/entities/pelicula/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula/service/pelicula.service';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { TarifaService } from 'app/entities/tarifa/service/tarifa.service';

import { FuncionUpdateComponent } from './funcion-update.component';

describe('Funcion Management Update Component', () => {
  let comp: FuncionUpdateComponent;
  let fixture: ComponentFixture<FuncionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let funcionService: FuncionService;
  let salaService: SalaService;
  let peliculaService: PeliculaService;
  let tarifaService: TarifaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FuncionUpdateComponent],
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
      .overrideTemplate(FuncionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuncionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    funcionService = TestBed.inject(FuncionService);
    salaService = TestBed.inject(SalaService);
    peliculaService = TestBed.inject(PeliculaService);
    tarifaService = TestBed.inject(TarifaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sala query and add missing value', () => {
      const funcion: IFuncion = { id: 456 };
      const sala: ISala = { id: 85239 };
      funcion.sala = sala;

      const salaCollection: ISala[] = [{ id: 69904 }];
      jest.spyOn(salaService, 'query').mockReturnValue(of(new HttpResponse({ body: salaCollection })));
      const additionalSalas = [sala];
      const expectedCollection: ISala[] = [...additionalSalas, ...salaCollection];
      jest.spyOn(salaService, 'addSalaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      expect(salaService.query).toHaveBeenCalled();
      expect(salaService.addSalaToCollectionIfMissing).toHaveBeenCalledWith(salaCollection, ...additionalSalas);
      expect(comp.salasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Pelicula query and add missing value', () => {
      const funcion: IFuncion = { id: 456 };
      const pelicula: IPelicula = { id: 60776 };
      funcion.pelicula = pelicula;

      const peliculaCollection: IPelicula[] = [{ id: 51478 }];
      jest.spyOn(peliculaService, 'query').mockReturnValue(of(new HttpResponse({ body: peliculaCollection })));
      const additionalPeliculas = [pelicula];
      const expectedCollection: IPelicula[] = [...additionalPeliculas, ...peliculaCollection];
      jest.spyOn(peliculaService, 'addPeliculaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      expect(peliculaService.query).toHaveBeenCalled();
      expect(peliculaService.addPeliculaToCollectionIfMissing).toHaveBeenCalledWith(peliculaCollection, ...additionalPeliculas);
      expect(comp.peliculasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tarifa query and add missing value', () => {
      const funcion: IFuncion = { id: 456 };
      const tarifa: ITarifa = { id: 45009 };
      funcion.tarifa = tarifa;

      const tarifaCollection: ITarifa[] = [{ id: 37887 }];
      jest.spyOn(tarifaService, 'query').mockReturnValue(of(new HttpResponse({ body: tarifaCollection })));
      const additionalTarifas = [tarifa];
      const expectedCollection: ITarifa[] = [...additionalTarifas, ...tarifaCollection];
      jest.spyOn(tarifaService, 'addTarifaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      expect(tarifaService.query).toHaveBeenCalled();
      expect(tarifaService.addTarifaToCollectionIfMissing).toHaveBeenCalledWith(tarifaCollection, ...additionalTarifas);
      expect(comp.tarifasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const funcion: IFuncion = { id: 456 };
      const sala: ISala = { id: 5332 };
      funcion.sala = sala;
      const pelicula: IPelicula = { id: 77508 };
      funcion.pelicula = pelicula;
      const tarifa: ITarifa = { id: 96873 };
      funcion.tarifa = tarifa;

      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(funcion));
      expect(comp.salasSharedCollection).toContain(sala);
      expect(comp.peliculasSharedCollection).toContain(pelicula);
      expect(comp.tarifasSharedCollection).toContain(tarifa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Funcion>>();
      const funcion = { id: 123 };
      jest.spyOn(funcionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(funcionService.update).toHaveBeenCalledWith(funcion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Funcion>>();
      const funcion = new Funcion();
      jest.spyOn(funcionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: funcion }));
      saveSubject.complete();

      // THEN
      expect(funcionService.create).toHaveBeenCalledWith(funcion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Funcion>>();
      const funcion = { id: 123 };
      jest.spyOn(funcionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ funcion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(funcionService.update).toHaveBeenCalledWith(funcion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSalaById', () => {
      it('Should return tracked Sala primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSalaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPeliculaById', () => {
      it('Should return tracked Pelicula primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeliculaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTarifaById', () => {
      it('Should return tracked Tarifa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTarifaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
