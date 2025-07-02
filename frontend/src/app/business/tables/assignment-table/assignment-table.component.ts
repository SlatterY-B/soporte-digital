import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { AssignmentService } from "../../../core/services/assignment.service";
import { DatePipe, NgClass, NgForOf, NgIf } from "@angular/common";
import { Paginator } from "primeng/paginator";

@Component({
  selector: 'app-assignment-table',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    NgClass,
    DatePipe,
    Paginator
  ],
  templateUrl: './assignment-table.component.html',
  styleUrl: './assignment-table.component.css'
})
export class AssignmentTableComponent implements OnInit {

  assignments: any[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  totalRecords: number = 0;
  rows: number = 5;
  currentPage: number = 0;


  constructor(private assignmentService: AssignmentService, private http: HttpClient) { }

  ngOnInit(): void {
    this.loadAssignments();

  }

  loadAssignments() {
    this.isLoading = true;
    this.assignmentService.getAssignmentsPage(this.currentPage, this.rows).subscribe({
      next: (data) => {
        this.assignments = data.content;
        this.totalRecords = data.totalElements;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load assignments. Please try again later.';
        console.error('Error loading assignments:', error);
        this.isLoading = false;
      }
    })
  }

  onPageChange(event: any) {
    this.currentPage = event.page;
    this.loadAssignments();
  }


  openDetails(assignment: any) {

  }
}
