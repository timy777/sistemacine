import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReporteService } from '../service/reporte.service';
import { IReporte, Reporte } from '../reporte.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

import { ReporteUpdateComponent } from './reporte-update.component';

describe('Reporte Management Update Component', () => {
  let comp: ReporteUpdateComponent;
  let fixture: ComponentFixture<ReporteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reporteService: ReporteService;
  let personaService: PersonaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReporteUpdateComponent],
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
      .overrideTemplate(ReporteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReporteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reporteService = TestBed.inject(ReporteService);
    personaService = TestBed.inject(PersonaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Persona query and add missing value', () => {
      const reporte: IReporte = { id: 'CBA' };
      const vendedor: IPersona = { id: 'e77717ac-b757-49b3-ae0f-65c9a6722d45' };
      reporte.vendedor = vendedor;

      const personaCollection: IPersona[] = [{ id: 'fd3ddb65-14ed-4f48-bd6b-9195dc0f590b' }];
      jest.spyOn(personaService, 'query').mockReturnValue(of(new HttpResponse({ body: personaCollection })));
      const additionalPersonas = [vendedor];
      const expectedCollection: IPersona[] = [...additionalPersonas, ...personaCollection];
      jest.spyOn(personaService, 'addPersonaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reporte });
      comp.ngOnInit();

      expect(personaService.query).toHaveBeenCalled();
      expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(personaCollection, ...additionalPersonas);
      expect(comp.personasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reporte: IReporte = { id: 'CBA' };
      const vendedor: IPersona = { id: 'fa6cb919-bfd9-4fd0-b0dd-e676bfaf1edd' };
      reporte.vendedor = vendedor;

      activatedRoute.data = of({ reporte });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reporte));
      expect(comp.personasSharedCollection).toContain(vendedor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reporte>>();
      const reporte = { id: 'ABC' };
      jest.spyOn(reporteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reporte }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reporteService.update).toHaveBeenCalledWith(reporte);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reporte>>();
      const reporte = new Reporte();
      jest.spyOn(reporteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reporte }));
      saveSubject.complete();

      // THEN
      expect(reporteService.create).toHaveBeenCalledWith(reporte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reporte>>();
      const reporte = { id: 'ABC' };
      jest.spyOn(reporteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reporteService.update).toHaveBeenCalledWith(reporte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonaById', () => {
      it('Should return tracked Persona primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackPersonaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
