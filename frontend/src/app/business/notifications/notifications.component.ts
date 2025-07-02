import { Component, OnInit } from '@angular/core';
import { CommonModule, NgClass, NgForOf } from "@angular/common";
import { NotificationsService } from "../../core/services/notifications.service";

// NUEVO: Cambio de nombre para evitar conflicto con Notification de navegador
export interface AppNotification {
  id: number;
  title: string;
  message: string;
  type: string;
  createdAt: string;
  read: boolean;
}

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [
    NgClass,
    NgForOf,
    CommonModule,
  ],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent implements OnInit {

  notifications: AppNotification[] = [];
  unreadNotifications: AppNotification[] = [];
  loading: boolean = true;

  constructor(private notificationService: NotificationsService) {}

  ngOnInit(): void {
    this.fetchNotifications();
    this.fetchUnreadNotifications();
  }

  fetchNotifications(): void {
    this.loading = true;
    this.notificationService.getAllNotifications().subscribe({
      next: (data: AppNotification[]) => {
        this.notifications = data.sort((a, b) => {
          return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching notifications:', error);
        this.loading = false;
      }
    });
  }

  fetchUnreadNotifications(): void {
    this.notificationService.getUnreadNotification().subscribe({
      next: (notif: AppNotification) => {
        this.unreadNotifications = notif ? [notif] : [];
      },
      error: (error) => {
        if (error.status !== 204) {
          console.error('Error fetching unread notification:', error);
        }
        this.unreadNotifications = [];
      }
    });
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id).subscribe({
      next: () => {
        const notif = this.notifications.find(n => n.id === id);
        if (notif) notif.read = true;
        this.unreadNotifications = this.unreadNotifications.filter(n => n.id !== id);
      },
      error: err => console.log("Error marking notification as read:", err)
    });
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications = this.notifications.map(n => ({ ...n, read: true }));
        this.unreadNotifications = [];
      },
      error: err => console.log("Error marking all notifications as read:", err)
    });
  }

  getUnreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  formatTimeAgo(dateStr: string): string {
    const now = new Date();
    const created = new Date(dateStr);
    const diff = Math.floor((now.getTime() - created.getTime()) / 1000);

    if (diff < 60) return `${diff} seconds ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return `${Math.floor(diff / 86400)}d ago`;
  }
}
