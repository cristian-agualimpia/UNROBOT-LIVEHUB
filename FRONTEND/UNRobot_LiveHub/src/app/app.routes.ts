import { Routes } from '@angular/router';

// --- Guards ---
// Importamos el guard que crearemos para proteger las rutas de jueces
import { judgeAuthGuard } from './core/guards/judge-auth.guard';

// --- Componentes Principales (Páginas) ---
import { LandingComponent } from './features/landing/landing.component';
import { TeamRegistrationComponent } from './features/team-registration/team-registration.component';

// --- Componentes del Flujo de Scoring ---
import { ScoringHubComponent } from './features/scoring/pages/scoring-hub/scoring-hub.component';
import { RoundListComponent } from './features/scoring/pages/round-list/round-list.component';
import { JudgeFormConfrontationComponent } from './features/scoring/pages/judge-form-confrontation/judge-form-confrontation.component';
import { JudgeFormIndividualComponent } from './features/scoring/pages/judge-form-individual/judge-form-individual.component';

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
    canActivate: [judgeAuthGuard], // Solo jueces pueden activar esta ruta
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
    // Esta ruta recibe el TIPO de categoría (ENFRENTAMIENTO o RONDA_INDIVIDUAL)
    // para saber qué servicio llamar (enfrentamientos o rondas-individuales)
    // y qué "mini-componente" renderizar.
    path: 'scoring/list/:categoryType/:categoryId',
    component: RoundListComponent,
    canActivate: [judgeAuthGuard],
    title: 'Lista de Rondas',
  },
  {
    // Ruta final para puntuar un ENFRENTAMIENTO específico
    path: 'scoring/judge/match/:matchId',
    component: JudgeFormConfrontationComponent,
    canActivate: [judgeAuthGuard],
    title: 'Puntuar Enfrentamiento',
  },
  {
    // Ruta final para puntuar una RONDA INDIVIDUAL específica
    path: 'scoring/judge/individual/:roundId',
    component: JudgeFormIndividualComponent,
    canActivate: [judgeAuthGuard],
    title: 'Puntuar Ronda Individual',
  },

  // --- Wildcard Route (Manejo de rutas no encontradas) ---
  // Si el usuario escribe una URL que no existe, lo redirige al inicio
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full',
  },
];