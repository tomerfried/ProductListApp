import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {
  DatabaseInteractionHandlerService
} from '../service/database-interaction-handler/database-interaction-handler.service';
import {Product} from '../model/product';
import {FormsModule} from '@angular/forms';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {FormComponent} from "../form/form.component";
import {ComponentsCommunicatorService} from '../service/components-communicator/components-communicator.service';

/**
 * ProductListComponent is a component that handles the list of products.
 * It provides functionalities such as fetching all products, deleting a product, editing a product, sorting and pagination.
 *
 * @property products - An array of Product objects that represents the list of products.
 * @property messageToUser - A string that represents any message that needs to be displayed to the user.
 * @property currentPage - A number that represents the current page in the pagination.
 * @property itemsPerPage - A number that represents the number of items to be displayed per page.
 *
 * @constructor
 * @param databaseInteractionHandlerService - Service for handling interactions with the database.
 * @param componentsCommunicatorService - Service for handling communication between components.
 */
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatProgressSpinnerModule, FormComponent],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  messageToUser = "";
  currentPage = 1;
  itemsPerPage = 10;

  constructor(
    private databaseInteractionHandlerService: DatabaseInteractionHandlerService,
    private componentsCommunicatorService: ComponentsCommunicatorService,
  ) {
  }

  ngOnInit(): void {
    this.getProducts();

    this.componentsCommunicatorService.productAdded$.subscribe((product: Product) => {
      this.onProductAdded(product);
    });

    this.componentsCommunicatorService.productEdited$.subscribe((product: Product) => {
      this.onProductEdited(product);
    });

    this.componentsCommunicatorService.messageUpdated$.subscribe((message: string) => {
      this.onMessageReceived(message);
    });
  }

  /**
   * Fetches all products from the database and assigns them to the products array.
   * A notifying message to the user is updated accordingly
   * @param {string} sortBy - Optional parameter to sort the products.
   */
  getProducts(sortBy: string = ''): void {
    this.databaseInteractionHandlerService.getAllProducts(sortBy).subscribe({
      next: (products) => {
        this.products = products;
        this.messageToUser = '';
      },
      error: (error) => {
        this.messageToUser = `Error: ${error}`;
        console.error('Error fetching products:', this.messageToUser);
      }
    });
  }

  /**
   * Deletes a product from the database and removes it from the products array.
   * A notifying message to the user is updated accordingly
   * @param {Product} product - The product to be deleted.
   */
  deleteProduct(product: Product): void {
    this.databaseInteractionHandlerService.deleteProduct(product.barcode).subscribe({
      next: () => {
        this.products = this.products.filter(p => p !== product);
        this.messageToUser = 'Product deleted successfully';
        this.checkAndChangePage();
      },
      error: (error) => {
        this.messageToUser = `Error: ${error}`;
        console.error('Error deleting product-list:', this.messageToUser);
      }
    });
  }

  /**
   * Updates the componentsCommunicatorService with the product to be edited,
   * so the component who is in charge of editing the product can receive it.
   * In this case, it is the FormComponent.
   * @param {Product} product - The product to be edited.
   */
  editProduct(product: Product): void {
    this.componentsCommunicatorService.notifyProductToEdit(product);
  }

  /**
   * When a product is added to the database, it is pushed to the products array.
   * @param product
   */
  onProductAdded(product: Product): void {
    this.products.push(product);
  }

  /**
   * When a product is edited in the database, it is updated in the products array.
   * @param product
   */
  onProductEdited(product: Product): void {
    const index = this.products.findIndex(p => p.barcode === product.barcode);
    this.products[index] = product;
  }

  /**
   * Updates the message to be displayed to the user.
   * @param message
   */
  onMessageReceived(message: string): void {
    this.messageToUser = message;
  }

  changePage(newPage: number): void {
    this.currentPage = newPage;
  }

  /**
   * Checks if the current page is valid and changes it if necessary.
   */
  checkAndChangePage(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = this.currentPage * this.itemsPerPage;
    if (this.products.slice(start, end).length === 0 && this.currentPage > 1) {
      this.currentPage--;
    }
  }
}
