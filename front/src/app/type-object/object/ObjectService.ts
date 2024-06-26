import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { Objects } from "./Object";
import { AuthService } from "../../login/authentification/AuthService";
import { jwtDecode } from "jwt-decode";
import { TypeObjects } from "../TypeObjects";
import { DetailObjects } from "./detail-object/DetailObjectData";
import { Country } from "./new-object/Country";

@Injectable({
    providedIn: "root"
})

export class ObjectService{
    private baseUrl = 'http://localhost:8083/sml';
    private http = inject(HttpClient);
    private authservice = inject(AuthService);

    constructor() {
    }
  
getAllObjects(idType:number): Observable<Objects[]>{
      const tokenString = this.authservice.getJWTToken();
      const decodedToken = jwtDecode(tokenString);
      const login = decodedToken.sub;
      return this.http.get<Objects[]>(`${this.baseUrl}/objects/${login}/${idType}`)
}

getCurrentTypeObject(id: number): Observable<TypeObjects> {
      return this.http.get<TypeObjects>(`${this.baseUrl}/types/${id}`);
  }

createNewObject(nomObjet: string, nomPays:string, nomTypeObjet:string): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');
    const body = new URLSearchParams();
    body.set('1-nomObjet', nomObjet);
    body.set('2-Pays', nomPays);
    body.set('3-Type', nomTypeObjet );
    const tokenString = this.authservice.getJWTToken();
    const decodedToken = jwtDecode(tokenString);
    const login = decodedToken.sub;


    return this.http.post(`${this.baseUrl}/objects/${login}`, body.toString(), { headers });
}

getAllCountries() : Observable<Country[]>{
    return this.http.get<Country[]>(`${this.baseUrl}/countries`);
  }

getAllAttributObject(idObjet: number, idType:number): Observable<DetailObjects[]>{
    const tokenString = this.authservice.getJWTToken();
    const decodedToken = jwtDecode(tokenString);
    const login = decodedToken.sub;
  return this.http.get<DetailObjects[]>(`${this.baseUrl}/attribute_object/${login}/${idType}/${idObjet}`)
}

getObjectById(idObjet: number){
  return this.http.get(`${this.baseUrl}/objects/${idObjet}/`)
}
}