/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [ "./src/**/*.{html,js,jsx,ts,tsx}",],
  theme: {
    extend: {
      colors: {
        primary: '#0477BF',
        accent: '#66ADD9',
        background: '#F2F2F2',
        shade: '#595959',
        dark:'#0D0D0D'
      }
    },
  },
  plugins: [],
}

