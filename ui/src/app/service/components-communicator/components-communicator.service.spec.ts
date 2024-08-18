import {TestBed} from '@angular/core/testing';
import {ComponentsCommunicatorService} from './components-communicator.service';
import {Product} from '../../model/product';

describe('ComponentsCommunicatorService', () => {
  let service: ComponentsCommunicatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComponentsCommunicatorService);
  });

  it('should emit product to edit', (done) => {
    const product: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.productToEdit$.subscribe(givenProduct => {
      expect(givenProduct).toEqual(product);
      done();
    });

    service.notifyProductToEdit(product);
  });

  it('should emit product To Edit', (done) => {
    const product: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.productToEdit$.subscribe(givenProduct => {
      expect(givenProduct).toEqual(product);
      done();
    });

    service.notifyProductToEdit(product);
  });

  it('should emit product added', (done) => {
    const product: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    service.productAdded$.subscribe(givenProduct => {
      expect(givenProduct).toEqual(product);
      done();
    });

    service.notifyProductAdded(product);
  });

  it('should emit message updated', (done) => {
    const message = 'Test message';

    service.messageUpdated$.subscribe(givenMessage => {
      expect(givenMessage).toEqual(message);
      done();
    });

    service.notifyMessageUpdated(message);
  });
});
