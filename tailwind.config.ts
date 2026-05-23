import type { Config } from "tailwindcss";

const config: Config = {
  darkMode: "class",
  content: ["./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#10151f",
        graphite: "#25313f",
        mist: "#eef3f7",
        petrol: "#075f68",
        limefuel: "#a7c957",
        warning: "#f7b801",
        danger: "#d64545",
        civic: "#2d6cdf"
      },
      boxShadow: {
        soft: "0 18px 60px rgba(16, 21, 31, 0.10)"
      }
    }
  },
  plugins: []
};

export default config;
