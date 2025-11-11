import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReporte, Reporte } from '../reporte.model';

import { ReporteService } from './reporte.service';

describe('Reporte Service', () => {
  let service: ReporteService;
  let httpMock: HttpTestingController;
  let elemDefault: IReporte;
  let expectedResult: IReporte | IReporte[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReporteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      tipo: 'AAAAAAA',
      fechaGeneracion: currentDate,
      descripcion: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaGeneracion: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Reporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
        },
        returnedFromService
      );

      service.create(new Reporte()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          tipo: 'BBBBBB',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reporte', () => {
      const patchObject = Object.assign(
        {
          fechaGeneracion: currentDate.format(DATE_FORMAT),
        },
        new Reporte()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          tipo: 'BBBBBB',
          fechaGeneracion: currentDate.format(DATE_FORMAT),
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaGeneracion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Reporte', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReporteToCollectionIfMissing', () => {
      it('should add a Reporte to an empty array', () => {
        const reporte: IReporte = { id: 'ABC' };
        expectedResult = service.addReporteToCollectionIfMissing([], reporte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reporte);
      });

      it('should not add a Reporte to an array that contains it', () => {
        const reporte: IReporte = { id: 'ABC' };
        const reporteCollection: IReporte[] = [
          {
            ...reporte,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addReporteToCollectionIfMissing(reporteCollection, reporte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reporte to an array that doesn't contain it", () => {
        const reporte: IReporte = { id: 'ABC' };
        const reporteCollection: IReporte[] = [{ id: 'CBA' }];
        expectedResult = service.addReporteToCollectionIfMissing(reporteCollection, reporte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reporte);
      });

      it('should add only unique Reporte to an array', () => {
        const reporteArray: IReporte[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '478c1f76-38f2-42bc-a236-2addfd992e5e' }];
        const reporteCollection: IReporte[] = [{ id: 'ABC' }];
        expectedResult = service.addReporteToCollectionIfMissing(reporteCollection, ...reporteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reporte: IReporte = { id: 'ABC' };
        const reporte2: IReporte = { id: 'CBA' };
        expectedResult = service.addReporteToCollectionIfMissing([], reporte, reporte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reporte);
        expect(expectedResult).toContain(reporte2);
      });

      it('should accept null and undefined values', () => {
        const reporte: IReporte = { id: 'ABC' };
        expectedResult = service.addReporteToCollectionIfMissing([], null, reporte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reporte);
      });

      it('should return initial array if no Reporte is added', () => {
        const reporteCollection: IReporte[] = [{ id: 'ABC' }];
        expectedResult = service.addReporteToCollectionIfMissing(reporteCollection, undefined, null);
        expect(expectedResult).toEqual(reporteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
