import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

/**
 * Función de guardia para proteger las rutas de los jueces.
 */
export const judgeAuthGuard: CanActivateFn = (route, state) => {
  
  console.log('%c--- Ejecutando JudgeAuthGuard ---', 'color: blue; font-weight: bold;');
  
  const router = inject(Router);

  // 1. Revisamos el valor CRUDO de sessionStorage
  const sessionValue = sessionStorage.getItem('isJudge');
  console.log('Valor leído de sessionStorage:', sessionValue);

  const isJudgeAuthenticated = sessionValue === 'true';
  console.log('¿Está autenticado? (debe ser true):', isJudgeAuthenticated);

  if (isJudgeAuthenticated) {
    // 2. Si el juez está autenticado, le damos paso
    console.log('%cAcceso PERMITIDO', 'color: green;');
    return true;
  } else {
    // 3. Si NO está autenticado, lo redirigimos
    console.error('%cAcceso DENEGADO. Redirigiendo a /', 'color: red;');
    router.navigate(['/']);
    return false;
  }
};