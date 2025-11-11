import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EstadoPelicula } from 'app/entities/enumerations/estado-pelicula.model';
import { IPelicula, Pelicula } from '../pelicula.model';

import { PeliculaService } from './pelicula.service';

describe('Pelicula Service', () => {
  let service: PeliculaService;
  let httpMock: HttpTestingController;
  let elemDefault: IPelicula;
  let expectedResult: IPelicula | IPelicula[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PeliculaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      titulo: 'AAAAAAA',
      sinopsis: 'AAAAAAA',
      duracion: 0,
      idioma: 'AAAAAAA',
      clasificacion: 'AAAAAAA',
      formato: 'AAAAAAA',
      estado: EstadoPelicula.EN_CARTELERA,
      imagenUrl: 'AAAAAAA',
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

    it('should create a Pelicula', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Pelicula()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pelicula', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          titulo: 'BBBBBB',
          sinopsis: 'BBBBBB',
          duracion: 1,
          idioma: 'BBBBBB',
          clasificacion: 'BBBBBB',
          formato: 'BBBBBB',
          estado: 'BBBBBB',
          imagenUrl: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pelicula', () => {
      const patchObject = Object.assign(
        {
          idioma: 'BBBBBB',
          formato: 'BBBBBB',
        },
        new Pelicula()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pelicula', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          titulo: 'BBBBBB',
          sinopsis: 'BBBBBB',
          duracion: 1,
          idioma: 'BBBBBB',
          clasificacion: 'BBBBBB',
          formato: 'BBBBBB',
          estado: 'BBBBBB',
          imagenUrl: 'BBBBBB',
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

    it('should delete a Pelicula', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPeliculaToCollectionIfMissing', () => {
      it('should add a Pelicula to an empty array', () => {
        const pelicula: IPelicula = { id: 'ABC' };
        expectedResult = service.addPeliculaToCollectionIfMissing([], pelicula);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pelicula);
      });

      it('should not add a Pelicula to an array that contains it', () => {
        const pelicula: IPelicula = { id: 'ABC' };
        const peliculaCollection: IPelicula[] = [
          {
            ...pelicula,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addPeliculaToCollectionIfMissing(peliculaCollection, pelicula);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pelicula to an array that doesn't contain it", () => {
        const pelicula: IPelicula = { id: 'ABC' };
        const peliculaCollection: IPelicula[] = [{ id: 'CBA' }];
        expectedResult = service.addPeliculaToCollectionIfMissing(peliculaCollection, pelicula);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pelicula);
      });

      it('should add only unique Pelicula to an array', () => {
        const peliculaArray: IPelicula[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '78bffd45-2326-484c-aeee-8a2b9c79beb4' }];
        const peliculaCollection: IPelicula[] = [{ id: 'ABC' }];
        expectedResult = service.addPeliculaToCollectionIfMissing(peliculaCollection, ...peliculaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pelicula: IPelicula = { id: 'ABC' };
        const pelicula2: IPelicula = { id: 'CBA' };
        expectedResult = service.addPeliculaToCollectionIfMissing([], pelicula, pelicula2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pelicula);
        expect(expectedResult).toContain(pelicula2);
      });

      it('should accept null and undefined values', () => {
        const pelicula: IPelicula = { id: 'ABC' };
        expectedResult = service.addPeliculaToCollectionIfMissing([], null, pelicula, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pelicula);
      });

      it('should return initial array if no Pelicula is added', () => {
        const peliculaCollection: IPelicula[] = [{ id: 'ABC' }];
        expectedResult = service.addPeliculaToCollectionIfMissing(peliculaCollection, undefined, null);
        expect(expectedResult).toEqual(peliculaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
