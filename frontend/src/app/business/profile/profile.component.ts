import { Component, OnInit } from '@angular/core';
import { UserService } from "../../core/services/user.service";


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  user: any = null;
  errorMessage: string = '';

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.userService.getCurrentUser().subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load user profile. Please try again later.';
        console.error('Error loading user profile:', err);
      }
    });
  }
}
