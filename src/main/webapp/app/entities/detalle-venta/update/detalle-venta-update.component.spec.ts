import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DetalleVentaService } from '../service/detalle-venta.service';
import { IDetalleVenta, DetalleVenta } from '../detalle-venta.model';
import { IFuncion } from 'app/entities/funcion/funcion.model';
import { FuncionService } from 'app/entities/funcion/service/funcion.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { DetalleVentaUpdateComponent } from './detalle-venta-update.component';

describe('DetalleVenta Management Update Component', () => {
  let comp: DetalleVentaUpdateComponent;
  let fixture: ComponentFixture<DetalleVentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleVentaService: DetalleVentaService;
  let funcionService: FuncionService;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DetalleVentaUpdateComponent],
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
      .overrideTemplate(DetalleVentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleVentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleVentaService = TestBed.inject(DetalleVentaService);
    funcionService = TestBed.inject(FuncionService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Funcion query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const funcion: IFuncion = { id: 90821 };
      detalleVenta.funcion = funcion;

      const funcionCollection: IFuncion[] = [{ id: 20725 }];
      jest.spyOn(funcionService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionCollection })));
      const additionalFuncions = [funcion];
      const expectedCollection: IFuncion[] = [...additionalFuncions, ...funcionCollection];
      jest.spyOn(funcionService, 'addFuncionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(funcionService.query).toHaveBeenCalled();
      expect(funcionService.addFuncionToCollectionIfMissing).toHaveBeenCalledWith(funcionCollection, ...additionalFuncions);
      expect(comp.funcionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Venta query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const venta: IVenta = { id: 78957 };
      detalleVenta.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 12675 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const funcion: IFuncion = { id: 36967 };
      detalleVenta.funcion = funcion;
      const venta: IVenta = { id: 84589 };
      detalleVenta.venta = venta;

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detalleVenta));
      expect(comp.funcionsSharedCollection).toContain(funcion);
      expect(comp.ventasSharedCollection).toContain(venta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleVentaService.update).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = new DetalleVenta();
      jest.spyOn(detalleVentaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(detalleVentaService.create).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleVentaService.update).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFuncionById', () => {
      it('Should return tracked Funcion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFuncionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVentaById', () => {
      it('Should return tracked Venta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVentaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
