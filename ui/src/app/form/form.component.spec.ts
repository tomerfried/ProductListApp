import {TestBed, fakeAsync, tick, ComponentFixture} from '@angular/core/testing';
import {FormComponent} from './form.component';
import {
  DatabaseInteractionHandlerService
} from '../service/database-interaction-handler/database-interaction-handler.service';
import {ComponentsCommunicatorService} from '../service/components-communicator/components-communicator.service';
import {Product} from '../model/product';
import {FormsModule} from '@angular/forms';
import {By} from '@angular/platform-browser';
import {of, throwError} from 'rxjs';

/**
 * This file contains unit tests for the FormComponent. It tests the functionality of adding and editing a product.
 *
 * The tests are designed to simulate user interactions such as clicking the Add or Edit button,
 * and to check the expected outcomes of these interactions. The tests also cover error handling.
 *
 * The DatabaseInteractionHandlerService and ComponentsCommunicatorService are mocked to isolate
 * the FormComponent for unit testing.
 */
describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let databaseInteractionHandlerService: jasmine.SpyObj<DatabaseInteractionHandlerService>;
  let componentsCommunicatorService: jasmine.SpyObj<ComponentsCommunicatorService>;

  beforeEach(() => {
    const spyProductService = jasmine.createSpyObj('DatabaseInteractionHandlerService', ['addProduct', 'updateProduct']);
    const spyCommunicatorService = jasmine.createSpyObj('CommunicatorService', ['notifyProductEdited', 'notifyProductAdded', 'notifyMessageUpdated']);

    TestBed.configureTestingModule({
      imports: [FormsModule],
      providers: [
        FormComponent,
        {provide: DatabaseInteractionHandlerService, useValue: spyProductService},
        {provide: ComponentsCommunicatorService, useValue: spyCommunicatorService}
      ]
    });

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    databaseInteractionHandlerService = TestBed.inject(DatabaseInteractionHandlerService) as jasmine.SpyObj<DatabaseInteractionHandlerService>;
    componentsCommunicatorService = TestBed.inject(ComponentsCommunicatorService) as jasmine.SpyObj<ComponentsCommunicatorService>;
  });

  it('should add product when Add Product button is clicked', fakeAsync(() => {
    const product: Product = {barcode: '123', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    const addedProduct: Product = {barcode: '123', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    databaseInteractionHandlerService.addProduct.and.returnValue(of(addedProduct));
    componentsCommunicatorService.notifyProductAdded.and.callThrough();

    component.product = product;

    const addButton = fixture.debugElement.query(By.css('#editOrAddButton')).nativeElement;
    addButton.click();
    tick();

    expect(databaseInteractionHandlerService.addProduct).toHaveBeenCalledWith(product);
    expect(componentsCommunicatorService.notifyMessageUpdated).toHaveBeenCalledWith("Product added successfully");
  }));

  it('should catch error and send its message when Add Product button is clicked with empty barcode', fakeAsync(() => {
    const productWithEmptyBarcode: Product = {barcode: '', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    const errorMessage = 'Barcode cannot be empty';

    databaseInteractionHandlerService.addProduct.and.returnValue(throwError(() => errorMessage));

    component.product = productWithEmptyBarcode;

    const addButton = fixture.debugElement.query(By.css('#editOrAddButton')).nativeElement;
    addButton.click();
    tick();

    expect(databaseInteractionHandlerService.addProduct).toHaveBeenCalledWith(productWithEmptyBarcode);
    expect(componentsCommunicatorService.notifyMessageUpdated).toHaveBeenCalledWith("Error: " + errorMessage);
  }));

  it('should edit product when Done Edit Product button is clicked', fakeAsync(() => {
    const product: Product = {barcode: '123', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    const editedProduct: Product = {barcode: '123', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    databaseInteractionHandlerService.updateProduct.and.returnValue(of(editedProduct));
    componentsCommunicatorService.notifyProductEdited.and.callThrough();

    component.product = product;
    component.preChangeBarcode = '123';
    component.isEditing = true;

    const editButton = fixture.debugElement.query(By.css('#editOrAddButton')).nativeElement;
    editButton.click();
    tick();

    expect(databaseInteractionHandlerService.updateProduct).toHaveBeenCalledWith(product, '123');
    expect(componentsCommunicatorService.notifyMessageUpdated).toHaveBeenCalledWith("Product edited successfully");
  }));

  it('should catch error and send its message when Done Edit Product button is clicked with empty barcode', fakeAsync(() => {
    const productWithEmptyBarcode: Product = {barcode: '', name: 'Test', image: '', rating: 0, price: 0, tags: []};
    const errorMessage = 'Barcode cannot be empty';

    databaseInteractionHandlerService.updateProduct.and.returnValue(throwError(() => errorMessage));

    component.product = productWithEmptyBarcode;
    component.preChangeBarcode = '';
    component.isEditing = true;

    const editButton = fixture.debugElement.query(By.css('#editOrAddButton')).nativeElement;
    editButton.click();
    tick();

    expect(databaseInteractionHandlerService.updateProduct).toHaveBeenCalledWith(productWithEmptyBarcode, '');
    expect(componentsCommunicatorService.notifyMessageUpdated).toHaveBeenCalledWith("Error: " + errorMessage);
  }));

  it('should return an array of tags when a string of tags is passed', () => {
    const tags = 'tag1, tag2, tag3';
    const expectedTags = ['tag1', 'tag2', 'tag3'];

    const result = FormComponent.parseTags(tags);

    expect(result).toEqual(expectedTags);
  });
});
