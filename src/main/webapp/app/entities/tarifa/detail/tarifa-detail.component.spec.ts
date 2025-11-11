import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TarifaDetailComponent } from './tarifa-detail.component';

describe('Tarifa Management Detail Component', () => {
  let comp: TarifaDetailComponent;
  let fixture: ComponentFixture<TarifaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TarifaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tarifa: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(TarifaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TarifaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tarifa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tarifa).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
