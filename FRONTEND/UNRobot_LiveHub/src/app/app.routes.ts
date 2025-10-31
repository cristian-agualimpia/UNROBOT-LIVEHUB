import { Routes } from '@angular/router';

// --- Guards ---
import { judgeAuthGuard } from './core/guards/judge-auth.guard';

// --- Componentes Principales (Páginas) ---
// CORREGIDO: Se quitó .component de las rutas
import { LandingComponent } from './features/landing/landing';
import { TeamRegistrationComponent } from './features/team-registration/team-registration';

// --- Componentes del Flujo de Scoring ---
// CORREGIDO: Se quitó .component de las rutas
import { ScoringHubComponent } from './features/scoring/pages/scoring-hub/scoring-hub';
import { RoundListComponent } from './features/scoring/pages/round-list/round-list';
import { JudgeFormConfrontationComponent } from './features/scoring/pages/judge-form-confrontation/judge-form-confrontation';
import { JudgeFormIndividualComponent } from './features/scoring/pages/judge-form-individual/judge-form-individual';

export const routes: Routes = [
  // --- Ruta de Bienvenida (Pública) ---
  {
    path: '',
    component: LandingComponent,
    title: 'UNRobot LiveHub - Bienvenida',
  },

  // --- Ruta de Registro de Equipos (Protegida) ---
  {
    path: 'register-team',
    component: TeamRegistrationComponent,
    canActivate: [judgeAuthGuard], 
    title: 'Registro de Equipos',
  },

  // --- Rutas del Panel de Jueces (Protegidas) ---
  {
    path: 'scoring',
    component: ScoringHubComponent,
    canActivate: [judgeAuthGuard],
    title: 'Panel de Jueces - Seleccionar Categoría',
  },
  {
    path: 'scoring/list/:categoryType/:categoryId',
    component: RoundListComponent,
    canActivate: [judgeAuthGuard],
    title: 'Lista de Rondas',
  },
  {
    path: 'scoring/judge/match/:matchId',
    component: JudgeFormConfrontationComponent,
    canActivate: [judgeAuthGuard],
    title: 'Puntuar Enfrentamiento',
  },
  {
    path: 'scoring/judge/individual/:roundId',
    component: JudgeFormIndividualComponent,
    canActivate: [judgeAuthGuard],
    title: 'Puntuar Ronda Individual',
  },

  // --- Wildcard Route ---
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];