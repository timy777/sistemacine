import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PromocionDetailComponent } from './promocion-detail.component';

describe('Promocion Management Detail Component', () => {
  let comp: PromocionDetailComponent;
  let fixture: ComponentFixture<PromocionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PromocionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ promocion: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(PromocionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PromocionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load promocion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.promocion).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
