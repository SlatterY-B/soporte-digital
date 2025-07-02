import { Component, OnInit } from '@angular/core';
import { SupportRequestService } from "../../../../core/services/support-request.service";
import { NgForOf } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { UserService } from "../../../../core/services/user.service";
import { AssignmentService } from "../../../../core/services/assignment.service";

@Component({
  selector: 'app-assign-agent-panel',
  imports: [
    NgForOf,
    FormsModule
  ],
  templateUrl: './assign-agent-panel.component.html',
  styleUrl: './assign-agent-panel.component.css'
})
export class AssignAgentPanelComponent implements OnInit {

  unassignedRequests: any[] = [];
  agents: any[] = [];

  constructor(private supportRequestService: SupportRequestService, private userService: UserService, private assignmentService: AssignmentService) { }

  ngOnInit() {
    this.loadUnassignedRequests();
    this.loadAgents();
  }

  loadUnassignedRequests(): void {
    this.supportRequestService.getUnassignedSupportRequests().subscribe({
      next: (data) => {
        this.unassignedRequests = data;
      },
      error: (error) => {
        console.error('Error loading unassigned requests:', error);
      }
    });
  }

  loadAgents(): void {
    this.userService.getAllAgents().subscribe({
      next: (data) => {
        this.agents = data;
      },
      error: (error) => {
        console.error('Error loading agents:', error);
      }
    });
  }

  assignAgent(requestId: number, agentId: number): void {
    if (!agentId) return;

    this.assignmentService.assignRequestToAgent(requestId, agentId).subscribe({
      next: () => {
        console.log(`✅ Request ${requestId} assigned to agent ${agentId}`);
        this.loadUnassignedRequests(); // Refresca la lista
      },
      error: (error) => {
        console.error('❌ Error assigning request:', error);
      }
    });
  }



}
