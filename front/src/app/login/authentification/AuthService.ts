import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {User} from "./auth";
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';





@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly JWT_TOKEN = 'JWT_TOKEN';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private baseUrl = 'http://localhost:8082/auth';
  private loggedUser ?: string;
  private http = inject(HttpClient);
  private jwtHelper = inject(JwtHelperService);
  private router = inject(Router);

  constructor() { }

  register(userDetails: User):Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/register`, userDetails, {responseType: 'text'} );
  }

  login(loginData: { login: string, password: string }):Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, loginData, {responseType: 'text'});
  }

  validateLogin(validateData: {login:string, codePin: number}): Observable<any> {
    const { login, codePin } = validateData;
    return this.http.post(`${this.baseUrl}/auth/${login}/codepin?CodePin=${codePin}`, null,{responseType: 'text'})
      .pipe(
        tap((tokens: any) => {
          this.doLoginUser(validateData.login, JSON.stringify(tokens));
          this.getCurrentAuthUser()
       })
      );
  }

  private doLoginUser(login: string, token: any){
    this.loggedUser = login;
    this.storeJwtToken(token);
    this.isAuthenticatedSubject.next(true);
  }

  private storeJwtToken(jwt: string) {
    localStorage.setItem(this.JWT_TOKEN, jwt);
  }

  getCurrentAuthUser(): Observable<any> {
    const tokenString = localStorage.getItem(this.JWT_TOKEN);
  if (!tokenString) {
    return of(null); 
  }
  const decodedToken = jwtDecode(tokenString);
  const token = JSON.parse(tokenString);
  const login = decodedToken.sub;
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  return this.http.get(`${this.baseUrl}/users/${login}`, { headers });
  
  }

  logout() {
    localStorage.removeItem(this.JWT_TOKEN);
    window.sessionStorage.clear;
    this.isAuthenticatedSubject.next(false);
    this.router.navigateByUrl('/login');
  }

  isLoggedIn() {
    return !!localStorage.getItem(this.JWT_TOKEN);
  }
  
  isTokenExpired() {
    const tokens = localStorage.getItem(this.JWT_TOKEN);
    if (!tokens) return true;
    const token = JSON.parse(tokens).access_token;
    const decoded = jwtDecode(token);
    if (!decoded.exp) return true;
    const expirationDate = decoded.exp * 1000;
    const now = new Date().getTime();

    return expirationDate < now;
  }

  getJWTToken(): string {
    return localStorage.getItem(this.JWT_TOKEN) || '';
  }
/*
  refreshToken() {
    let tokens: any = localStorage.getItem(this.JWT_TOKEN);
    if (!tokens) return;
    tokens = JSON.parse(tokens);
    let refreshToken = tokens.refresh_token;
    return this.http
      .post<any>(`${this.baseUrl}/auth/refresh-token`, {
        refreshToken,
      })
      .pipe(tap((tokens: any) => this.storeJwtToken(JSON.stringify(tokens))));
  }
*/
}
