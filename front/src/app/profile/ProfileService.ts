import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class ProfileService{
    private baseUrl = 'http://localhost:8082/auth';

  constructor(private http: HttpClient) {}

  requestPassword(login: string, password:string) {
    const headers = new HttpHeaders().set('Content-Type', `application/x-www-form-urlencoded`);
    const body = new URLSearchParams();
    body.set('NewPassword', password);

    return this.http.post(`${this.baseUrl}/users/${login}/request_password`, body.toString(), {headers});
  }

  requestEmail(login:string, email: string){
    const headers = new HttpHeaders().set('Content-Type', `application/x-www-form-urlencoded`);
    const body = new URLSearchParams();
    body.set('NewEmail', email);
    return this.http.post(`${this.baseUrl}/users/${login}/request_email`, body.toString(), {headers});
  }

}