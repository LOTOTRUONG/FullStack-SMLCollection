import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { Users } from "./Users";

@Injectable({
    providedIn: 'root'
})
export class AdminService{
    private baseUrl = 'http://localhost:8082/auth';

  constructor(private http: HttpClient) {}

  getAllUsers(token: string): Observable<Users[]>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Users[]>(`${this.baseUrl}/admin/allUsers`, { headers })

  }

  deactivateUser(login: string, token: string) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = {
      headers: headers,
      responseType: 'Text' as 'json'
    }
    return this.http.put(`${this.baseUrl}/admin/${login}/deactivate`, null, options)
    .pipe(
      catchError(this.handleError)  // Handle error
    );
  }

  reactivateUser(login:string, token:string){
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = {
      headers: headers,
      responseType: 'Text' as 'json'
    }
    return this.http.put(`${this.baseUrl}/admin/${login}/reactivate`, null, options)
    .pipe(
      catchError(this.handleError)  // Handle error
    );
  }

  updateUser(login:string, token:string){
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = {
      headers: headers,
      responseType: 'Text' as 'json'
    }
    return this.http.put(`${this.baseUrl}/admin/${login}/updateRole`, null, options)
    .pipe(
      catchError(this.handleError)  // Handle error
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }
    
}