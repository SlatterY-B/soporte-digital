import { Component, OnInit } from '@angular/core';
import { ActivityService } from "../../../core/services/activity.service";
import { DatePipe, NgForOf, NgIf } from "@angular/common";

@Component({
  selector: 'app-activity-table',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    NgIf
  ],
  templateUrl: './activity-table.component.html',
  styleUrl: './activity-table.component.css'
})
export class ActivityTableComponent implements OnInit {

  activities: any[] = [];

  constructor(private activityService: ActivityService) {}

  ngOnInit(): void {
    this.getAllActivities();
  }

  getAllActivities(): void {
    this.activityService.getAllActivities(0).subscribe({
      next: (data) => {
        this.activities = data;
      },
      error: (error) => {
        console.error('Error fetching activities:', error);
      }
    })
  }

}
