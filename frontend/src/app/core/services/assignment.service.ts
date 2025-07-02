import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AssignmentService {

  private API_URL = 'http://localhost:8080/assignments';

  constructor(private http: HttpClient) { }

  getAllAssignments(): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers= { 'Authorization': `Bearer ${token}` };

    return this.http.get(`${ this.API_URL }`, { headers });

  }

  getAssignmentsByAgent(agentId: number): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
    };
    return this.http.get(`${ this.API_URL }/agent/${ agentId }`, { headers });
  }



  markAssignmentCompleted(assignment: number): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.post(`${ this.API_URL }/${ assignment }/complete`, {}, { headers });
  }

  getAssignmentsPage(page: number, size: number): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get(`${ this.API_URL }/page?page=${ page }&size=${ size }`, { headers });
  }

  getAssignmentBySupportRequestId(supportRequestId: number): Observable<any> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders ({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.get(`${ this.API_URL }/by-support-request/${ supportRequestId }`, { headers });
  }


  //test


  assignRequestToAgent(requestId: number, agentId: number): Observable<any> {
    const token = localStorage.getItem('auth_token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    const body = { agentId };

    return this.http.put(
      `http://localhost:8080/assignments/${requestId}/assign`,
      body,
      { headers }
    );
  }





}
