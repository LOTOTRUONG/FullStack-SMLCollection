import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
  })

export class NewTypeService {
    private baseUrl = 'http://localhost:8083/sml';
    private http = inject(HttpClient);

    createTypeObject(typeObjectName: string): Observable<any> {
        const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
        const body = new URLSearchParams();
        body.set('nomType', typeObjectName);
    
        return this.http.post(`${this.baseUrl}/types`, body.toString(), { headers });
      }
}

