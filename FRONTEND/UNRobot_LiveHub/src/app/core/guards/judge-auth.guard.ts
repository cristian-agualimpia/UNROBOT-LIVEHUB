import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

/**
 * Función de guardia para proteger las rutas de los jueces.
 */
export const judgeAuthGuard: CanActivateFn = (route, state) => {
  
  // Inyectamos el Router de Angular para poder redirigir al usuario
  const router = inject(Router);

  // 1. Revisamos si existe la bandera 'isJudge' en el sessionStorage
  const isJudgeAuthenticated = sessionStorage.getItem('isJudge') === 'true';

  if (isJudgeAuthenticated) {
    // 2. Si el juez está autenticado, le damos paso
    return true;
  } else {
    // 3. Si NO está autenticado, lo redirigimos a la página de inicio ('/')
    console.warn('Acceso denegado: Se requiere autenticación de juez.');
    router.navigate(['/']);
    return false;
  }
};