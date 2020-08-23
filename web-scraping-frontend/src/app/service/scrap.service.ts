import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {throwError, Observable} from 'rxjs';
import {map, catchError} from 'rxjs/operators';
import { RequestModel } from '../model/request.model';
import { ResponseModel } from '../model/response.model';
import { ListModel } from '../model/list.model';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScrapService {

  constructor(private httpService: HttpClient) {
  }
 
  request(request: RequestModel): Observable<object> {
    return this.httpService.get(`/api/request/?url=${request.url}&maxLevels=${request.maxLevels}`).pipe(catchError(this.handleError));    
  }  

  list(): Observable<ListModel[]> {
    return this.httpService.get<ListModel[]>(`/api/list`).pipe(catchError(this.handleError));
  }  

  view(id: number): Observable<ResponseModel[]> {
    return this.httpService.get<ResponseModel[]>(`/api/retrieve/${id}`).pipe(catchError(this.handleError));
  }    

  poll(list: ListModel[]): Observable<ListModel[]> {    
    
    let waitList = list.filter(function(item: ListModel) {
      return item.status == 'LOADING';
    });

    if (waitList.length == 0) {
      return of<ListModel[]>([]);
    }

    let params = '';

    waitList.forEach((item: ListModel) => {
      if (params) {
        params += '&';
      }
      else {
        params += '?';
      }

      params += 'urls=' + item.url;
    });

    return this.httpService.get<ListModel[]>(`/api/poll/${params}`).pipe(catchError(this.handleError));
  }  

  private handleError(error: HttpErrorResponse) {
    let errorMessage = "";
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = 'An error occurred:', error.error.message;
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      errorMessage = `Backend returned code ${error.status}, body was: ${error.error}`;
    }

    console.error(errorMessage);
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.<br/><br/>' + errorMessage);
  };  

}