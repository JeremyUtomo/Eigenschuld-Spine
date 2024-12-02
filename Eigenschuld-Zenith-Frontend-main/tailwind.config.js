/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        "primaryBlue": "#6D7FAC",
        "secondaryDarkBlue": "#202859"
      },
      rotate: {
        '18': '18deg',
        '348': '348deg',
        '354': '354deg'
      },
      boxShadow: {
        '3xl': '0 35px 60px -15px rgba(0, 0, 0, 0.3)',
      }
    },
  },
  plugins: [],
}

