// tailwind.config.js

// ¡ASEGÚRATE DE QUE ESTA LÍNEA EXISTA!
const defaultTheme = require('tailwindcss/defaultTheme');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}", // Esta línea es VITAL
  ],
  theme: {
    extend: {
      // 1. Configuración de Fuente
      fontFamily: {
        'sans': ['Barlow', ...defaultTheme.fontFamily.sans],
        'heading': ['Barlow', ...defaultTheme.fontFamily.sans],
      },
      
      // 2. Configuración de Color
      colors: {
        'unrobot-darkest': '#171A4A',
        'unrobot-dark': '#2F2C79',
        'unrobot-purple-deep': '#4C007D',
        'unrobot-purple': '#7F00B2',
        'unrobot-mauve': '#8966A4',
        'unrobot-light': '#EDECF7',
      }
    },
  },
  plugins: [],
};