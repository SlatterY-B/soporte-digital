import { Component, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { UserService } from "../../../core/services/user.service";
import { Router } from "@angular/router";
import { PaginatorModule } from 'primeng/paginator';

@Component({
  selector: 'app-users-table',
  standalone: true,
  imports: [CommonModule, PaginatorModule],
  templateUrl: './users-table.component.html',
  styleUrl: './users-table.component.css'
})
export class UsersTableComponent implements OnInit  {

  users: any[] = [];
  selectedUser: any = null;

  //properties for pagination
  totalRecords: number = 0;
  rows: number = 6;
  currentPage: number = 0;
  role: string | null = '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.role = localStorage.getItem("user_role");
    if (this.role === 'ADMIN') {
      this.loadUsers();
    }
  }

  loadUsers() {
    this.userService.getPage(this.currentPage, this.rows).subscribe({
      next: (data) => {
        this.users = data.content;
        this.totalRecords = data.totalElements;
      },
      error: (error) => console.error('Error loading users:', error)
    });
  }

  onPageChange(event: any) {
    console.log('📌 Nueva página:', event.page);
    this.currentPage = event.page;
    this.loadUsers();
  }

  getRoleBadgeClass(role: string): string {
    switch (role) {
      case 'ADMIN':
        return 'bg-yellow-100 text-yellow-800 font-bold';
      case 'AGENT':
        return 'bg-purple-100 text-purple-800';
      case 'CUSTOMER':
        return 'bg-blue-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

}
