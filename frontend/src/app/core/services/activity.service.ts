import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ActivityService {

  private API_URL = 'http://localhost:8080/activity';
  constructor(private http: HttpClient) { }

  createActivity(activity: any): Observable<any> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${ this.API_URL }`, activity, { headers });
  }

  getAllActivities(id: number): Observable<any> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.get(`${ this.API_URL }`, { headers });
  }

  getActivitiesByAssignmentId(id: number): Observable<any> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.get(`${ this.API_URL }/assigned/${ id }`, { headers });
  }



}
