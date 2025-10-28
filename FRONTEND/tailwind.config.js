/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}", // Esto le dice a Tailwind que escanee tus archivos
  ],
  theme: {
    extend: {
      // Aquí están tus colores globales
      colors: {
        'unrobot-blue': '#0A4A8F',
        'unrobot-gold': '#FDB913',
        'brand-dark': '#222222',
        'brand-light': '#F5F5F5',
        'brand-accent': '#FF4136',
      },
      // Aquí están tus fuentes globales
      fontFamily: {
        'sans': ['Roboto', 'sans-serif'],
        'heading': ['Montserrat', 'sans-serif'],
      },
    },
  },
  plugins: [],
}