import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { SupportRequestService } from "../../../../core/services/support-request.service";
import { AuthService } from "../../../../core/services/auth.service";

@Component({
  selector: 'app-support-request-form',
  imports: [
    FormsModule

  ],
  templateUrl: './support-request-form.component.html',
  styleUrl: './support-request-form.component.css'
})
export class SupportRequestFormComponent {

  protected readonly window = window;
  title: string = '';
  description: string = '';
  requestType: string = 'BUG';
  //protected readonly onsubmit = onsubmit;

  constructor(private supportRequestService: SupportRequestService, private authService: AuthService) {}

  onSubmit(): void {
    const body = {
      title: this.title,
      description: this.description,
      requestType: this.requestType,
    };
    this.supportRequestService.createSupportRequest(body).subscribe({
      next: (response) => {
        alert('✅Support request created successfully!');
        this.resetForm();
        window.location.reload();
      },
      error: (error) => {
        console.error('Error creating support request:', error);
        alert('❌Failed to create support request. Please try again later.');
      }
    });
  }

  resetForm(): void {
    this.title = '';
    this.description = '';
    this.requestType = 'BUG';
  }





}
