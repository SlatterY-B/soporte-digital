import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersTableComponent } from "./users-table/users-table.component";
import { SupportRequestTableComponent } from "./support-request-table/support-request-table.component";
import { AssignmentTableComponent } from "./assignment-table/assignment-table.component";
import { ActivityTableComponent } from "./activity-table/activity-table.component";
import { AuthService } from "../../core/services/auth.service";

@Component({
  selector: 'app-tables',
  standalone: true,
  imports: [CommonModule, UsersTableComponent, SupportRequestTableComponent, AssignmentTableComponent, ActivityTableComponent],
  templateUrl: './tables.component.html',
  styleUrl: './tables.component.css'
})
export class TablesComponent implements OnInit {

  activeTab: string | null = null;

  role: string | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit() {

     this.role = this.authService.getRole();

    console.log('🎯 ROLE:', this.role);

    // Set the initial active tab based on the user's role
    if (this.role === 'ADMIN') {
      this.activeTab = 'users';
    } else if (this.role === 'AGENT') {
      this.activeTab = 'assignment';
    } else if (this.role === 'CUSTOMER') {
      this.activeTab = 'support';
    } else {
      this.activeTab = null; // Default or fallback tab
    }
  }
}
