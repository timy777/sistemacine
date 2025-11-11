import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';
import { ISala, Sala } from '../sala.model';

import { SalaService } from './sala.service';

describe('Sala Service', () => {
  let service: SalaService;
  let httpMock: HttpTestingController;
  let elemDefault: ISala;
  let expectedResult: ISala | ISala[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      nombre: 'AAAAAAA',
      capacidad: 0,
      tipo: TipoSala.DOSD,
      estado: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Sala', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Sala()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sala', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          capacidad: 1,
          tipo: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sala', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          capacidad: 1,
          estado: 'BBBBBB',
        },
        new Sala()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sala', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          capacidad: 1,
          tipo: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Sala', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSalaToCollectionIfMissing', () => {
      it('should add a Sala to an empty array', () => {
        const sala: ISala = { id: 'ABC' };
        expectedResult = service.addSalaToCollectionIfMissing([], sala);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sala);
      });

      it('should not add a Sala to an array that contains it', () => {
        const sala: ISala = { id: 'ABC' };
        const salaCollection: ISala[] = [
          {
            ...sala,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, sala);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sala to an array that doesn't contain it", () => {
        const sala: ISala = { id: 'ABC' };
        const salaCollection: ISala[] = [{ id: 'CBA' }];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, sala);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sala);
      });

      it('should add only unique Sala to an array', () => {
        const salaArray: ISala[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '8196ce3a-f7c7-4893-b984-c7276440be1f' }];
        const salaCollection: ISala[] = [{ id: 'ABC' }];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, ...salaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sala: ISala = { id: 'ABC' };
        const sala2: ISala = { id: 'CBA' };
        expectedResult = service.addSalaToCollectionIfMissing([], sala, sala2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sala);
        expect(expectedResult).toContain(sala2);
      });

      it('should accept null and undefined values', () => {
        const sala: ISala = { id: 'ABC' };
        expectedResult = service.addSalaToCollectionIfMissing([], null, sala, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sala);
      });

      it('should return initial array if no Sala is added', () => {
        const salaCollection: ISala[] = [{ id: 'ABC' }];
        expectedResult = service.addSalaToCollectionIfMissing(salaCollection, undefined, null);
        expect(expectedResult).toEqual(salaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
