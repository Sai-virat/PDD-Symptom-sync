/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        wellness: {
          charcoal: '#1A1612',
          card: '#2D2620',
          white: '#FAF9F6',
          pink: '#FFF8F5',
          cream: '#FFFDF9',
          navy: '#101D42',
        },
        richOrange: '#F4511E',
        smoothPurple: '#7E57C2',
        healthGreen: '#2E7D32',
        indicator: {
          yellow: '#D4AF37',
          red: '#B71C1C',
          green: '#1B5E20',
        }
      },
    },
  },
  plugins: [],
};
