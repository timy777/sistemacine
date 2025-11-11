import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoSala } from 'app/entities/enumerations/tipo-sala.model';
import { ITarifa, Tarifa } from '../tarifa.model';

import { TarifaService } from './tarifa.service';

describe('Tarifa Service', () => {
  let service: TarifaService;
  let httpMock: HttpTestingController;
  let elemDefault: ITarifa;
  let expectedResult: ITarifa | ITarifa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TarifaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      monto: 0,
      diaSemana: 'AAAAAAA',
      tipoSala: TipoSala.DOSD,
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

    it('should create a Tarifa', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tarifa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tarifa', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          monto: 1,
          diaSemana: 'BBBBBB',
          tipoSala: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tarifa', () => {
      const patchObject = Object.assign(
        {
          monto: 1,
          diaSemana: 'BBBBBB',
        },
        new Tarifa()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tarifa', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          monto: 1,
          diaSemana: 'BBBBBB',
          tipoSala: 'BBBBBB',
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

    it('should delete a Tarifa', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTarifaToCollectionIfMissing', () => {
      it('should add a Tarifa to an empty array', () => {
        const tarifa: ITarifa = { id: 'ABC' };
        expectedResult = service.addTarifaToCollectionIfMissing([], tarifa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tarifa);
      });

      it('should not add a Tarifa to an array that contains it', () => {
        const tarifa: ITarifa = { id: 'ABC' };
        const tarifaCollection: ITarifa[] = [
          {
            ...tarifa,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addTarifaToCollectionIfMissing(tarifaCollection, tarifa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tarifa to an array that doesn't contain it", () => {
        const tarifa: ITarifa = { id: 'ABC' };
        const tarifaCollection: ITarifa[] = [{ id: 'CBA' }];
        expectedResult = service.addTarifaToCollectionIfMissing(tarifaCollection, tarifa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tarifa);
      });

      it('should add only unique Tarifa to an array', () => {
        const tarifaArray: ITarifa[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'b9c00b13-da0d-4682-b827-9de2eb3a01d5' }];
        const tarifaCollection: ITarifa[] = [{ id: 'ABC' }];
        expectedResult = service.addTarifaToCollectionIfMissing(tarifaCollection, ...tarifaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tarifa: ITarifa = { id: 'ABC' };
        const tarifa2: ITarifa = { id: 'CBA' };
        expectedResult = service.addTarifaToCollectionIfMissing([], tarifa, tarifa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tarifa);
        expect(expectedResult).toContain(tarifa2);
      });

      it('should accept null and undefined values', () => {
        const tarifa: ITarifa = { id: 'ABC' };
        expectedResult = service.addTarifaToCollectionIfMissing([], null, tarifa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tarifa);
      });

      it('should return initial array if no Tarifa is added', () => {
        const tarifaCollection: ITarifa[] = [{ id: 'ABC' }];
        expectedResult = service.addTarifaToCollectionIfMissing(tarifaCollection, undefined, null);
        expect(expectedResult).toEqual(tarifaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
