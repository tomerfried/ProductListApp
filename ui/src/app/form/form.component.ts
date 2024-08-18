import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Product} from '../model/product';
import {DatabaseInteractionHandlerService} from '../service/database-interaction-handler/database-interaction-handler.service';
import {ComponentsCommunicatorService} from '../service/components-communicator/components-communicator.service';

/**
 * FormComponent is responsible for handling the form operations in the application.
 * It provides functionalities to add and edit products.
 *
 * @property isEditing - Flag to check if the form is in edit mode
 * @property product - Product object which is filled by the form, in order to add or edit a product
 * @property tags - String representation of product tags, filled by the form and parsed to individual tags
 * @property preChangeBarcode - Used in cases where there's a need to store the barcode of the product before editing it
 */
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  standalone: true,
  imports: [FormsModule],
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {
  tags: string = '';
  preChangeBarcode: string = "";
  isEditing: boolean = false;
  product: Product = {barcode: '', name: '', image: '', rating: 0, price: 0, tags: []};

  constructor(
    private databaseInteractionHandlerService: DatabaseInteractionHandlerService,
    private componentsCommunicatorService: ComponentsCommunicatorService
  ) {
  }

  /**
   * Lifecycle hook which organizes the form every time a product is sent to be edited.
   */
  ngOnInit(): void {
    this.componentsCommunicatorService.productToEdit$.subscribe((product: Product) => {
      this.product = product;
      this.preChangeBarcode = product.barcode;
      this.tags = this.product.tags.join(', ');
      this.isEditing = true;
      window.scrollTo(0, 0);
    });
  }

  /**
   * Method to add a product to the list.
   * It sends the product to the database and notifies the user about the result.
   */
  addProduct(): void {
    this.product.tags = FormComponent.parseTags(this.tags);
    this.databaseInteractionHandlerService.addProduct(this.product).subscribe({
      next: (addedProduct) => {
        this.componentsCommunicatorService.notifyProductAdded(addedProduct);
        this.resetForm();
        this.componentsCommunicatorService.notifyMessageUpdated("Product added successfully");
      },
      error: (error) => {
        const message = `Error: ${error}`;
        console.error('Error adding product:', message);
        this.componentsCommunicatorService.notifyMessageUpdated(message);
      }
    });
  }

  /**
   * Method to edit a product in the database.
   * It sends the product to the database and notifies the user about the result.
   */
  editProduct(): void {
    this.product.tags = FormComponent.parseTags(this.tags);
    this.databaseInteractionHandlerService.updateProduct(this.product, this.preChangeBarcode).subscribe({
      next: (editedProduct) => {
        this.componentsCommunicatorService.notifyProductEdited(editedProduct);
        this.resetForm();
        this.isEditing = false;
        this.componentsCommunicatorService.notifyMessageUpdated("Product edited successfully");
      },
      error: (error) => {
        const message = `Error: ${error}`;
        console.error('Error editing product:', message);
        this.isEditing = false;
        this.componentsCommunicatorService.notifyMessageUpdated(message);
      }
    });
  }

  /**
   * Cleans the form fields and resets the form to its initial state.
   */
  private resetForm(): void {
    this.product = {barcode: '', name: '', image: '', rating: 0, price: 0, tags: []};
    this.tags = "";
    this.preChangeBarcode = '';
  }

  /**
   * Method to parse tags from a string.
   * @param {string} tags - String of tags separated by commas.
   * @returns {string[]} Array of tags.
   */
  static parseTags(tags: string): string[] {
    if (tags === "") {
      return [];
    }
    return tags.split(',').map(tag => tag.trim());
  }
}
