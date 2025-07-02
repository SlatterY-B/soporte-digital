import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API_URL = 'http://localhost:8080/users';

  constructor(private http: HttpClient) { }

  getCurrentUser(): Observable<any> {

    const token = localStorage.getItem('auth_token');
    console.log('🔑 Token:', token);

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    })

    return this.http.get(`${ this.API_URL }/me`, { headers });

  }

  getAllUsers(): Observable<any> {
    const token = localStorage.getItem('auth_token');
    console.log('🔑 Token for get all users:', token);

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    return this.http.get(`${ this.API_URL }`, { headers });

  }

  getPage(page: number, size:number): Observable<any> {
    const url = `${this.API_URL}?page=${page}&size=${size}`;
    const token = localStorage.getItem('auth_token');

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.get<any>(url, { headers });
  }

  getAllAgents(): Observable<any> {
    const token = localStorage.getItem('auth_token');
    console.log('🔑 Token for get all agents:', token);

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    return this.http.get(`${ this.API_URL }/agents`, { headers });
  }






}
