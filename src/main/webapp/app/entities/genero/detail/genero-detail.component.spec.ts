import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GeneroDetailComponent } from './genero-detail.component';

describe('Genero Management Detail Component', () => {
  let comp: GeneroDetailComponent;
  let fixture: ComponentFixture<GeneroDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GeneroDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ genero: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(GeneroDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GeneroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load genero on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.genero).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
