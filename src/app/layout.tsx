import type { Metadata, Viewport } from "next";
import { Analytics } from "@vercel/analytics/react";
import { ServiceWorkerRegister } from "@/components/ServiceWorkerRegister";
import "./globals.css";

export const metadata: Metadata = {
  title: "Vigia de Posto | Preco, seguranca e qualidade de postos",
  description:
    "Consulte precos, reputacao e seguranca de postos de combustivel no Brasil. Mapa gratuito, anonimo, sem anuncios.",
  manifest: "/manifest.json",
  appleWebApp: {
    capable: true,
    statusBarStyle: "black-translucent",
    title: "Vigia de Posto"
  },
  icons: {
    icon: "/icon-192.svg",
    apple: "/icon-192.svg"
  }
};

export const viewport: Viewport = {
  themeColor: "#075f68",
  width: "device-width",
  initialScale: 1,
  viewportFit: "cover"
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR">
      <body>
        <ServiceWorkerRegister />
        {children}
        <Analytics />
      </body>
    </html>
  );
}
