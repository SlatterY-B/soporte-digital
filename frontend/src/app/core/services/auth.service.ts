import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { Observable, tap } from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private LOGIN_URL = 'http://localhost:8080/api/auth/login';
  private tokenKey = 'auth_token';

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  login(email: string, password: string): Observable<any> {
    return this.httpClient.post<any>(this.LOGIN_URL, { email, password }).pipe(
      tap(response => {
        console.log('✅ Respuesta login:', response);
        localStorage.setItem(this.tokenKey, response.token);
        localStorage.setItem('user_role', response.role);
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): string | null {
    const roleFromStorage = localStorage.getItem('user_role');

    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return localStorage.getItem('user_role');

    } catch (e) {
      console.error('❌ Error parsing token:', e);
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp * 1000;
      return Date.now() < exp;
    } catch (e) {
      return false;
    }
  }

  register(fullName: string, email: string, password: string): Observable<any> {
    const REGISTER_URL = 'http://localhost:8080/api/auth/register';
    return this.httpClient.post<any>(REGISTER_URL, { fullName, email, password });
  }

}
