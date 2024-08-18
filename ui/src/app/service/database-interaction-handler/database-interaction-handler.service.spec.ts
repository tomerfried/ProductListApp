import {TestBed} from '@angular/core/testing';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {DatabaseInteractionHandlerService} from './database-interaction-handler.service';
import {Product} from '../../model/product';
import {provideHttpClient} from "@angular/common/http";

describe('DatabaseInteractionHandlerService', () => {
  let service: DatabaseInteractionHandlerService;
  let httpTestingController: HttpTestingController;
  const productsDatabaseUrl = 'http://localhost:8081/api/products';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [DatabaseInteractionHandlerService,
        provideHttpClient(),
        provideHttpClientTesting()]
    });

    service = TestBed.inject(DatabaseInteractionHandlerService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should return all products when getAllProducts is called', (done) => {
    const mockProducts: Product[] = [
      {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []},
      {barcode: '456', name: 'Product 2', image: '', rating: 0, price: 0, tags: []},
    ];

    service.getAllProducts().subscribe(products => {
      expect(products).toEqual(mockProducts);
      done();
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '?sortBy=name');
    expect(req.request.method).toEqual('GET');
    req.flush(mockProducts);
  });

  it('should throw error when getAllProducts is called', (done) => {
    const errorMessage = 'bad request';

    service.getAllProducts().subscribe({
      next: () => {
      },
      error: error => {
        expect(error).toEqual(errorMessage);
        done();
      }
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '?sortBy=name');
    req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});
  });

  it('should update a product when updateProduct is called', (done) => {
    const mockProduct: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.updateProduct(mockProduct, '123').subscribe(product => {
      expect(product).toEqual(mockProduct);
      done();
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/123');
    expect(req.request.method).toEqual('PATCH');
    expect(req.request.body).toEqual(mockProduct);
    req.flush(mockProduct);
  });

  it('should throw an error when updateProduct is called with invalid data', (done) => {
    const mockProduct: Product = {barcode: '', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};
    const errorMessage = 'Error: Barcode cannot be empty';

    service.updateProduct(mockProduct, '').subscribe({
      next: () => {
      },
      error: error => {
        expect(error).toEqual(errorMessage);
        done();
      }
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/');
    req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});
  });

  it('should delete a product when deleteProduct is called', (done) => {
    service.deleteProduct('123').subscribe(() => {
      done();
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/123');
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });

  it('should throw an error when deleteProduct is called with invalid barcode', (done) => {
    const errorMessage = 'Error: Barcode cannot be empty';

    service.deleteProduct('').subscribe({
      next: () => {
      },
      error: error => {
        expect(error).toEqual(errorMessage);
        done();
      }
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/');
    req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});
  });

  it('should add a product when addProduct is called', (done) => {
    const mockProduct: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.addProduct(mockProduct).subscribe(product => {
      expect(product).toEqual(mockProduct);
      done();
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(mockProduct);
    req.flush(mockProduct);
  });

  it('should throw an error when addProduct is called with invalid data', (done) => {
    const mockProduct: Product = {barcode: '', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};
    const errorMessage = 'Error: Barcode cannot be empty';

    service.addProduct(mockProduct).subscribe({
      next: () => {
      },
      error: error => {
        expect(error).toEqual(errorMessage);
        done();
      }
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl);
    req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});
  });

  it('should get a product when getProduct is called', (done) => {
    const mockProduct: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.getProduct('123').subscribe(product => {
      expect(product).toEqual(mockProduct);
      done();
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/123');
    expect(req.request.method).toEqual('GET');
    req.flush(mockProduct);
  });

  it('should throw an error when getProduct is called with invalid barcode', (done) => {
    const errorMessage = 'Error: Barcode cannot be empty';

    service.getProduct('').subscribe({
      next: () => {
      },
      error: error => {
        expect(error).toEqual(errorMessage);
        done();
      }
    });

    const req = httpTestingController.expectOne(productsDatabaseUrl + '/');
    req.flush(errorMessage, {status: 400, statusText: 'Bad Request'});
  });
});
