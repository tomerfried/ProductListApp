import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {Product} from '../../model/product';

@Injectable({
  providedIn: 'root'
})
/**
 * A service class that provides a communication channel between different components in the application.
 * In this case, it is used to communicate between the product list component and the product form component.
 * It uses RxJS Subjects to emit events and data to subscribers.
 *
 * The service provides methods to notify subscribers when a product is to be edited, when a product has been edited,
 * when a product has been added, and when a message is updated.
 *
 * @property {Subject<Product>} productToEditSource - A subject that emits the product to be edited.
 * @property {Subject<Product>} productEditedSource - A subject that emits the product that has been edited.
 * @property {Subject<Product>} productAddedSource - A subject that emits the product that has been added.
 * @property {Subject<string>} messageUpdatedSource - A subject that emits when a message is updated.
 **/
export class ComponentsCommunicatorService {
  private productToEditSource = new Subject<Product>();
  private productEditedSource = new Subject<Product>();
  private productAddedSource = new Subject<Product>();
  private messageUpdatedSource = new Subject<string>();

  productToEdit$ = this.productToEditSource.asObservable();
  productEdited$ = this.productEditedSource.asObservable();
  productAdded$ = this.productAddedSource.asObservable();
  messageUpdated$ = this.messageUpdatedSource.asObservable();

  notifyProductToEdit(product: Product): void {
    this.productToEditSource.next(product);
  }

  notifyProductEdited(product: Product): void {
    this.productEditedSource.next(product);
  }

  notifyProductAdded(product: Product): void {
    this.productAddedSource.next(product);
  }

  notifyMessageUpdated(message: string): void {
    this.messageUpdatedSource.next(message);
  }
}
