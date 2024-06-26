import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { TypeObjects } from "./TypeObjects";
import { Observable } from "rxjs";
import { AuthService } from "../login/authentification/AuthService";

@Injectable({
    providedIn: "root"
})

export class TypeObjectService{
    private baseUrl = 'http://localhost:8083/sml';
    http = inject(HttpClient);
    authservice = inject(AuthService);

    constructor() {
    }
    
    getAllTypes(): Observable<TypeObjects[]>{
      return this.http.get<TypeObjects[]>(`${this.baseUrl}/types`)
    }

    getCurrentType(idType:number): Observable<TypeObjects>{
      return this.http.get<TypeObjects>(`${this.baseUrl}/types/${idType}`);
    }
    
    

    /*
    addNewObject(){
      const tokenString = this.authService.getJWTToken();
      const decodedToken = jwtDecode(tokenString);
      const login = decodedToken.sub;
      return this.http.post<any>(`${this.baseUrl}/objects/${login}`, {});
    }
    */
}