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
      const funcion: IFuncion = { id: 'CBA' };
      const sala: ISala = { id: 'db00bc94-cc4f-4afc-af0f-e094d7cbf772' };
      funcion.sala = sala;

      const salaCollection: ISala[] = [{ id: '8380f54b-1f89-436b-90f1-29f440b10557' }];
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
      const funcion: IFuncion = { id: 'CBA' };
      const pelicula: IPelicula = { id: '98c89e68-7332-4f2f-bbf6-f1f54a150218' };
      funcion.pelicula = pelicula;

      const peliculaCollection: IPelicula[] = [{ id: '0125de0b-ed4e-4d49-ab7a-0a91a8a64ef2' }];
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
      const funcion: IFuncion = { id: 'CBA' };
      const tarifa: ITarifa = { id: '76ff44ca-2af5-4517-ae8c-9a419ad867e7' };
      funcion.tarifa = tarifa;

      const tarifaCollection: ITarifa[] = [{ id: 'a6cb11d9-3099-4693-8fed-876b0af718f4' }];
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
      const funcion: IFuncion = { id: 'CBA' };
      const sala: ISala = { id: 'ea42e5fe-bd54-451b-9caf-c2dad64ce595' };
      funcion.sala = sala;
      const pelicula: IPelicula = { id: 'd6d88c41-f743-41b0-a765-5a7bd83d822a' };
      funcion.pelicula = pelicula;
      const tarifa: ITarifa = { id: '62a951de-a0ba-4411-95ab-99a54d943852' };
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
      const funcion = { id: 'ABC' };
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
      const funcion = { id: 'ABC' };
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
        const entity = { id: 'ABC' };
        const trackResult = comp.trackSalaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPeliculaById', () => {
      it('Should return tracked Pelicula primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackPeliculaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTarifaById', () => {
      it('Should return tracked Tarifa primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackTarifaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
