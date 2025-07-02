import { Component, OnInit } from '@angular/core';
import { SupportRequestService } from "../../../core/services/support-request.service";
import { CommonModule, DatePipe, NgForOf, NgIf } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { AuthService } from "../../../core/services/auth.service";
import { SupportRequestFormComponent } from "./support-request-form/support-request-form.component";
import { PaginatorModule } from 'primeng/paginator';
import { ActivityService } from "../../../core/services/activity.service";
import { AssignmentService } from "../../../core/services/assignment.service";
import { AssignAgentPanelComponent } from "./assign-agent-panel/assign-agent-panel.component";


@Component({
  selector: 'app-support-request-table',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    NgIf,
    FormsModule,
    CommonModule,
    SupportRequestFormComponent,
    PaginatorModule,
    AssignAgentPanelComponent
  ],
  templateUrl: './support-request-table.component.html',
  styleUrl: './support-request-table.component.css'
})
export class SupportRequestTableComponent implements OnInit {

  requests: any[] = [];
  updatedStatuses: { [id: number]: string } = {};
  role: string | null = null;
  showCreateForm: boolean = false;

  totalRecords: number = 0;
  rows: number = 4;
  currentPage: number = 0;

  showAssignPanel: boolean = false;

  toggleAssignPanel(): void {
    this.showAssignPanel = !this.showAssignPanel;
  }

  onCreateRequestClick(): void {
    this.showCreateForm = true;
  }


  constructor(private supportRequestService: SupportRequestService, private authService: AuthService,
              private activityService: ActivityService, private assignmentService: AssignmentService) { }

  ngOnInit() {
    this.role = this.authService.getRole();
    this.loadRequests();
  }

  loadRequests():void {
    this.supportRequestService.getSupportRequestPage(this.currentPage, this.rows).subscribe({
      next:(data) => {
        this.requests = data.content;
        this.totalRecords = data.totalElements;
      },
      error:(error) => {
        console.error('Error fetching support requests:', error);
      }
    });
  }

  onPageChange(event: any): void {
    this.currentPage = event.page;
    this.loadRequests();
  }

  onStatusChange(id: number, newStatus: string): void {
    this.updatedStatuses[id] = newStatus;
  }

  updateStatus(id: number): void {
    const newStatus = this.updatedStatuses[id];
    const request = this.requests.find(r => r.id === id);

    if (!newStatus || !request) {
      alert('No status selected or status has not changed');
      return;
    }
    const body = {
      title: request.title,
      description: request.description,
      requestType: request.requestType,
      requestStatus: newStatus,
    };

    this.supportRequestService.updateSupportRequest(id, body).subscribe({
      next:() => {
        alert(`Status for request ID ${id} updated to: ${newStatus}`);
        this.loadRequests();

        if (newStatus === 'COMPLETED') {
          this.assignmentService.getAssignmentBySupportRequestId(id).subscribe({
            next: (assignment) => {
              //const assignment = assignments.find((a : any) => a.supportRequest.id === id);
              if (assignment) {
                const activity = {
                  activityDate: new Date(),
                  activityDescription: `Support request with ID ${id} has been completed.`,
                  hoursWorked: 0,
                  assignment: assignment.id
                };
                this.activityService.createActivity(activity).subscribe({
                  next: () => console.log(`Activity created for completed request ID ${id}`),
                  error: (error) => console.error('Error creating activity:', error)
                });
              } else {
                console.warn(`No activity created for completed request ID ${id}`);
              }
            },
            error: (error) => console.error('Error fetching assignments:', error)
          });
        }
      },
      error:(error) => {
        console.error('Error updating support request:', error);
        alert(`Failed to update status for request ID ${id}`);
      }

    });

  }


}
