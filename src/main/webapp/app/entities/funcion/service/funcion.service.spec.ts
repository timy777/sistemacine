import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFuncion, Funcion } from '../funcion.model';

import { FuncionService } from './funcion.service';

describe('Funcion Service', () => {
  let service: FuncionService;
  let httpMock: HttpTestingController;
  let elemDefault: IFuncion;
  let expectedResult: IFuncion | IFuncion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FuncionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      fecha: currentDate,
      horaInicio: currentDate,
      horaFin: currentDate,
      precio: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Funcion', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Funcion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Funcion', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
          precio: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Funcion', () => {
      const patchObject = Object.assign(
        {
          horaFin: currentDate.format(DATE_TIME_FORMAT),
          precio: 1,
        },
        new Funcion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Funcion', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
          precio: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Funcion', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFuncionToCollectionIfMissing', () => {
      it('should add a Funcion to an empty array', () => {
        const funcion: IFuncion = { id: 'ABC' };
        expectedResult = service.addFuncionToCollectionIfMissing([], funcion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcion);
      });

      it('should not add a Funcion to an array that contains it', () => {
        const funcion: IFuncion = { id: 'ABC' };
        const funcionCollection: IFuncion[] = [
          {
            ...funcion,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addFuncionToCollectionIfMissing(funcionCollection, funcion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Funcion to an array that doesn't contain it", () => {
        const funcion: IFuncion = { id: 'ABC' };
        const funcionCollection: IFuncion[] = [{ id: 'CBA' }];
        expectedResult = service.addFuncionToCollectionIfMissing(funcionCollection, funcion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcion);
      });

      it('should add only unique Funcion to an array', () => {
        const funcionArray: IFuncion[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '4f01a570-5df5-4ab4-a440-5ee66d4dfc84' }];
        const funcionCollection: IFuncion[] = [{ id: 'ABC' }];
        expectedResult = service.addFuncionToCollectionIfMissing(funcionCollection, ...funcionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const funcion: IFuncion = { id: 'ABC' };
        const funcion2: IFuncion = { id: 'CBA' };
        expectedResult = service.addFuncionToCollectionIfMissing([], funcion, funcion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(funcion);
        expect(expectedResult).toContain(funcion2);
      });

      it('should accept null and undefined values', () => {
        const funcion: IFuncion = { id: 'ABC' };
        expectedResult = service.addFuncionToCollectionIfMissing([], null, funcion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(funcion);
      });

      it('should return initial array if no Funcion is added', () => {
        const funcionCollection: IFuncion[] = [{ id: 'ABC' }];
        expectedResult = service.addFuncionToCollectionIfMissing(funcionCollection, undefined, null);
        expect(expectedResult).toEqual(funcionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
