import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { AppNotification } from "../../business/notifications/notifications.component";
 // NUEVO

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  private API_URL = 'http://localhost:8080/notifications';

  constructor(private http: HttpClient) { }

  getAllNotifications(): Observable<AppNotification[]> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.get<AppNotification[]>(`${ this.API_URL}`, { headers });
  }

  getUnreadNotification(): Observable<AppNotification> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.get<AppNotification>(`${ this.API_URL }/unread`, { headers });
  }

  markAsRead(id: number): Observable<void> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.put<void>(`${ this.API_URL }/${id}/read`, {}, { headers });
  }

  markAllAsRead(): Observable<void> {
    const token = localStorage.getItem("auth_token");
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.put<void>(`${ this.API_URL }/read-all`, {}, { headers }); // ARREGLADO: faltaba body vacío
  }

}
