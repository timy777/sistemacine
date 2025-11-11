import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGenero, Genero } from '../genero.model';

import { GeneroService } from './genero.service';

describe('Genero Service', () => {
  let service: GeneroService;
  let httpMock: HttpTestingController;
  let elemDefault: IGenero;
  let expectedResult: IGenero | IGenero[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GeneroService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
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

    it('should create a Genero', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Genero()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Genero', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Genero', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        new Genero()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Genero', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should delete a Genero', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGeneroToCollectionIfMissing', () => {
      it('should add a Genero to an empty array', () => {
        const genero: IGenero = { id: 'ABC' };
        expectedResult = service.addGeneroToCollectionIfMissing([], genero);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(genero);
      });

      it('should not add a Genero to an array that contains it', () => {
        const genero: IGenero = { id: 'ABC' };
        const generoCollection: IGenero[] = [
          {
            ...genero,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addGeneroToCollectionIfMissing(generoCollection, genero);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Genero to an array that doesn't contain it", () => {
        const genero: IGenero = { id: 'ABC' };
        const generoCollection: IGenero[] = [{ id: 'CBA' }];
        expectedResult = service.addGeneroToCollectionIfMissing(generoCollection, genero);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(genero);
      });

      it('should add only unique Genero to an array', () => {
        const generoArray: IGenero[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'ea1aa4ae-0945-4525-976d-02b1447eab9b' }];
        const generoCollection: IGenero[] = [{ id: 'ABC' }];
        expectedResult = service.addGeneroToCollectionIfMissing(generoCollection, ...generoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const genero: IGenero = { id: 'ABC' };
        const genero2: IGenero = { id: 'CBA' };
        expectedResult = service.addGeneroToCollectionIfMissing([], genero, genero2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(genero);
        expect(expectedResult).toContain(genero2);
      });

      it('should accept null and undefined values', () => {
        const genero: IGenero = { id: 'ABC' };
        expectedResult = service.addGeneroToCollectionIfMissing([], null, genero, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(genero);
      });

      it('should return initial array if no Genero is added', () => {
        const generoCollection: IGenero[] = [{ id: 'ABC' }];
        expectedResult = service.addGeneroToCollectionIfMissing(generoCollection, undefined, null);
        expect(expectedResult).toEqual(generoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
