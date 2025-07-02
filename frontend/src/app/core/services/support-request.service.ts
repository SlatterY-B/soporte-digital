import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SupportRequestService {

  private API_URL = 'http://localhost:8080/support-requests';

  constructor(private http: HttpClient) { }

  getAllSupportRequests(): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = { 'Authorization': `Bearer ${token}` };

    return this.http.get(`${ this.API_URL }`, { headers });
  }

  updateSupportRequest(id: number, updatedDate: any): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.put(`${ this.API_URL }/${id}`, updatedDate, { headers });

  }

  createSupportRequest(data: any): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
    return this.http.post(`${ this.API_URL }`, data, { headers });
  }

  getSupportRequestPage(page: number, size: number): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
    };
    return this.http.get(`${ this.API_URL }/page?page=${ page }&size=${ size }`, { headers });
  }


  getUnassignedSupportRequests(): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
    };
    return this.http.get(`${ this.API_URL }/unassigned`, { headers });
  }


}
