import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetalleVentaDetailComponent } from './detalle-venta-detail.component';

describe('DetalleVenta Management Detail Component', () => {
  let comp: DetalleVentaDetailComponent;
  let fixture: ComponentFixture<DetalleVentaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetalleVentaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ detalleVenta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DetalleVentaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DetalleVentaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load detalleVenta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.detalleVenta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
