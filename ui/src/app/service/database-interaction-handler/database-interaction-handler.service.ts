import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpErrorResponse} from '@angular/common/http';
import {Product} from '../../model/product';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
/**
 * This service is responsible for handling the interaction with the database.
 */
export class DatabaseInteractionHandlerService {
  private productsUrl = 'http://localhost:8081/api/products';

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  constructor(private http: HttpClient) {
  }

  getAllProducts(sortBy: string = 'name'): Observable<Product[]> {
    let url = this.productsUrl;
    if (sortBy) {
      url += `?sortBy=${sortBy}`;
    }
    return this.http.get<Product[]>(url).pipe(
      catchError(this.handleError)
    );
  }

  getProduct(barcode: string): Observable<Product> {
    const url = `${this.productsUrl}/${barcode}`;
    return this.http.get<Product>(url).pipe(
      catchError(this.handleError)
    );
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.productsUrl, product, this.httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  updateProduct(product: Product, oldBarcode: string): Observable<Product> {
    const url = `${this.productsUrl}/${oldBarcode}`;
    return this.http.patch<Product>(url, product, this.httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  deleteProduct(barcode: string): Observable<void> {
    const url = `${this.productsUrl}/${barcode}`;
    return this.http.delete<void>(url, {...this.httpOptions, responseType: 'text' as 'json'}).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    return throwError(() => error.error);
  }
}
