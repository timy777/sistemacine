import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FuncionDetailComponent } from './funcion-detail.component';

describe('Funcion Management Detail Component', () => {
  let comp: FuncionDetailComponent;
  let fixture: ComponentFixture<FuncionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FuncionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ funcion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FuncionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FuncionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load funcion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.funcion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
