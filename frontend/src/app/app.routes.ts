import { Routes } from '@angular/router';
import { AuthGuard } from "./core/guards/auth.guard";
import { AuthenticatedGuard } from "./core/guards/authenticated.guard";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./shared/components/layout/layout.component'),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./business/dashboard/dashboard.component').then(m => m.DashboardComponent),
        canActivate: [AuthGuard]
      },
      {
        path:'profile',
        loadComponent: () => import('./business/profile/profile.component').then(m => m.ProfileComponent),
        canActivate: [AuthGuard]
      },
      {
        path:'tables',
        loadComponent: () => import('./business/tables/tables.component').then(m => m.TablesComponent),
        canActivate: [AuthGuard]
      },
      {
        path:'notifications',
        loadComponent: () => import('./business/notifications/notifications.component').then(m => m.NotificationsComponent),
        canActivate: [AuthGuard]
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }

    ]
  },
  {
    path: 'login',
    loadComponent: () => import('./business/authentication/login/login.component'),
    canActivate: [AuthenticatedGuard]
  },
  {
    path: 'sign-up',
    loadComponent: () => import('./business/authentication/register/register.component'),
    canActivate: [AuthenticatedGuard]

  },

  {
    path:'**',
    redirectTo: 'dashboard'
  }
];
