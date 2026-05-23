import type { AppProps } from "next/app";
import Head from "next/head";
import { ServiceWorkerRegister } from "@/components/ServiceWorkerRegister";
import "@/app/globals.css";

export default function App({ Component, pageProps }: AppProps) {
  return (
    <>
      <Head>
        <title>Vigia de Posto | Observatório colaborativo de combustíveis</title>
        <meta
          name="description"
          content="WebApp/PWA para transparência, fiscalização cidadã e reputação colaborativa de postos de combustível no Brasil."
        />
        <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover" />
        <meta name="theme-color" content="#075f68" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
        <link rel="manifest" href="/manifest.json" />
        <link rel="icon" href="/icon-192.svg" />
        <link rel="apple-touch-icon" href="/icon-192.svg" />
      </Head>
      <ServiceWorkerRegister />
      <Component {...pageProps} />
    </>
  );
}
