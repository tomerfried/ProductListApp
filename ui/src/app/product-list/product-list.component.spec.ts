import {TestBed, ComponentFixture, fakeAsync, tick} from '@angular/core/testing';
import {of} from 'rxjs';
import {ProductListComponent} from './product-list.component';
import {
  DatabaseInteractionHandlerService
} from '../service/database-interaction-handler/database-interaction-handler.service';
import {ComponentsCommunicatorService} from '../service/components-communicator/components-communicator.service';
import {Product} from '../model/product';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {By} from "@angular/platform-browser";
import {provideHttpClient} from "@angular/common/http";

/**
 * This file contains unit tests for the ProductListComponent. It tests the functionality of getting,
 * deleting and editing a product. It also tests the pagination functionality.
 *
 * The tests are designed to simulate user interactions such as clicking the Delete or Edit button,
 * and to check the expected outcomes of these interactions.
 *
 * The DatabaseInteractionHandlerService and ComponentsCommunicatorService are mocked to isolate
 * the ProductListComponent for unit testing.
 */
describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let databaseInteractionHandlerService: jasmine.SpyObj<DatabaseInteractionHandlerService>;
  let componentsCommunicatorService: jasmine.SpyObj<ComponentsCommunicatorService>;

  beforeEach(async () => {
    const databaseInteractionHandlerServiceSpy = jasmine.createSpyObj('DatabaseInteractionHandlerService', ['getAllProducts', 'deleteProduct']);

    await TestBed.configureTestingModule({
      declarations: [],
      imports: [],
      providers: [
        {provide: DatabaseInteractionHandlerService, useValue: databaseInteractionHandlerServiceSpy},
        ComponentsCommunicatorService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    databaseInteractionHandlerService = TestBed.inject(DatabaseInteractionHandlerService) as jasmine.SpyObj<DatabaseInteractionHandlerService>;
    componentsCommunicatorService = TestBed.inject(ComponentsCommunicatorService) as jasmine.SpyObj<ComponentsCommunicatorService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
  });


  it('should retrieve products when getProducts is called', fakeAsync(() => {
    const products: Product[] = [
      {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []},
      {barcode: '456', name: 'Product 2', image: '', rating: 0, price: 0, tags: []}
    ];

    databaseInteractionHandlerService.getAllProducts.and.returnValue(of(products));

    component.getProducts();
    tick();
    fixture.detectChanges();

    expect(component.products).toEqual(products);

    const displayedProducts = fixture.debugElement.queryAll(By.css('tbody tr'));

    expect(displayedProducts.length).toBe(2);
    expect(displayedProducts[0].nativeElement.textContent).toContain('Product 1');
    expect(displayedProducts[1].nativeElement.textContent).toContain('Product 2');
  }));

  it('should remove product from products array when delete button is pressed', fakeAsync(() => {
    const product1 = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};
    const product2 = {barcode: '456', name: 'Product 2', image: '', rating: 0, price: 0, tags: []};
    const products: Product[] = [product1, product2];

    databaseInteractionHandlerService.getAllProducts.and.returnValue(of(products));

    component.ngOnInit();
    tick();
    fixture.detectChanges();

    databaseInteractionHandlerService.deleteProduct.and.returnValue(of(void 0));

    const deleteButton = fixture.debugElement.query(By.css(`#delete-button-123`));
    deleteButton.triggerEventHandler('click', null);

    tick();
    fixture.detectChanges();

    expect(databaseInteractionHandlerService.deleteProduct).toHaveBeenCalledWith("123");
    expect(component.products).toEqual([product2]);
    expect(component.messageToUser).toBe('Product deleted successfully');

  }));


  it('should call edit product when edit button is pressed', fakeAsync(() => {
    const product: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};

    databaseInteractionHandlerService.getAllProducts.and.returnValue(of([product]));
    component.ngOnInit();
    tick();
    fixture.detectChanges();

    spyOn(component, 'editProduct').and.callThrough();

    const editButton = fixture.debugElement.query(By.css(`#edit-button-${product.barcode}`));
    editButton.triggerEventHandler('click', null);

    tick();
    fixture.detectChanges();

    expect(component.editProduct).toHaveBeenCalledWith(product);
  }));

  it('should update product variable which is designated to fill the form input fields', fakeAsync(() => {
    const product: Product = {barcode: '123', name: 'Product 1', image: '', rating: 0, price: 0, tags: []};
    let receivedProduct: Product = {barcode: '', name: '', image: '', rating: 0, price: 0, tags: []};

    componentsCommunicatorService.productToEdit$.subscribe((product: Product) => {
      receivedProduct = product;
    });

    component.editProduct(product);
    tick();
    expect(receivedProduct).toEqual(product);
  }));


  it('should show only the 11th product after clicking "Next"', fakeAsync(() => {
    const products = getMockProducts();
    databaseInteractionHandlerService.getAllProducts.and.returnValue(of(products));

    component.getProducts();
    tick();
    fixture.detectChanges();

    component.currentPage = 1;
    fixture.detectChanges();

    const nextButton = fixture.debugElement.query(By.css('.pagination button:last-child'));
    nextButton.nativeElement.click();
    tick();
    fixture.detectChanges();

    const displayedProducts = fixture.debugElement.queryAll(By.css('tbody tr'));

    expect(displayedProducts.length).toBe(1);
    expect(displayedProducts[0].nativeElement.textContent).toContain('Product 11');
  }));

  it('should return to the previous page and show products 1 to 10 after clicking "Previous"', fakeAsync(() => {
    const products = getMockProducts();
    databaseInteractionHandlerService.getAllProducts.and.returnValue(of(products));

    component.getProducts();
    tick();
    fixture.detectChanges();

    component.currentPage = 2;
    fixture.detectChanges();

    const previousButton = fixture.debugElement.query(By.css('.pagination button:first-child'));
    previousButton.nativeElement.click();
    tick();
    fixture.detectChanges();

    const displayedProducts = fixture.debugElement.queryAll(By.css('tbody tr'));

    expect(displayedProducts.length).toBe(10);
    expect(displayedProducts[0].nativeElement.textContent).toContain('Product 1');
    expect(displayedProducts[9].nativeElement.textContent).toContain('Product 10');
  }));

});

function getMockProducts() {
  return Array.from({length: 11}, (_, i) => ({
    barcode: `barcode${i + 1}`,
    name: `Product ${i + 1}`,
    image: '',
    rating: 0,
    price: 0,
    tags: []
  }));
}
