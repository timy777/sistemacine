import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetalleVenta, DetalleVenta } from '../detalle-venta.model';

import { DetalleVentaService } from './detalle-venta.service';

describe('DetalleVenta Service', () => {
  let service: DetalleVentaService;
  let httpMock: HttpTestingController;
  let elemDefault: IDetalleVenta;
  let expectedResult: IDetalleVenta | IDetalleVenta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DetalleVentaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      asiento: 'AAAAAAA',
      precioUnitario: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DetalleVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DetalleVenta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetalleVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asiento: 'BBBBBB',
          precioUnitario: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetalleVenta', () => {
      const patchObject = Object.assign({}, new DetalleVenta());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetalleVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asiento: 'BBBBBB',
          precioUnitario: 1,
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

    it('should delete a DetalleVenta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDetalleVentaToCollectionIfMissing', () => {
      it('should add a DetalleVenta to an empty array', () => {
        const detalleVenta: IDetalleVenta = { id: 123 };
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], detalleVenta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleVenta);
      });

      it('should not add a DetalleVenta to an array that contains it', () => {
        const detalleVenta: IDetalleVenta = { id: 123 };
        const detalleVentaCollection: IDetalleVenta[] = [
          {
            ...detalleVenta,
          },
          { id: 456 },
        ];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, detalleVenta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetalleVenta to an array that doesn't contain it", () => {
        const detalleVenta: IDetalleVenta = { id: 123 };
        const detalleVentaCollection: IDetalleVenta[] = [{ id: 456 }];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, detalleVenta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleVenta);
      });

      it('should add only unique DetalleVenta to an array', () => {
        const detalleVentaArray: IDetalleVenta[] = [{ id: 123 }, { id: 456 }, { id: 62701 }];
        const detalleVentaCollection: IDetalleVenta[] = [{ id: 123 }];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, ...detalleVentaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detalleVenta: IDetalleVenta = { id: 123 };
        const detalleVenta2: IDetalleVenta = { id: 456 };
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], detalleVenta, detalleVenta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detalleVenta);
        expect(expectedResult).toContain(detalleVenta2);
      });

      it('should accept null and undefined values', () => {
        const detalleVenta: IDetalleVenta = { id: 123 };
        expectedResult = service.addDetalleVentaToCollectionIfMissing([], null, detalleVenta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detalleVenta);
      });

      it('should return initial array if no DetalleVenta is added', () => {
        const detalleVentaCollection: IDetalleVenta[] = [{ id: 123 }];
        expectedResult = service.addDetalleVentaToCollectionIfMissing(detalleVentaCollection, undefined, null);
        expect(expectedResult).toEqual(detalleVentaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
