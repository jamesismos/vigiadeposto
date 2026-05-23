import Link from "next/link";
import { WifiOff } from "lucide-react";

export default function OfflinePage() {
  return (
    <main className="grid min-h-screen place-items-center bg-[#10151f] px-5 text-white">
      <section className="max-w-md rounded-lg border border-white/10 bg-white/8 p-6 text-center">
        <WifiOff className="mx-auto h-10 w-10 text-limefuel" />
        <h1 className="mt-4 text-2xl font-black">Você está offline</h1>
        <p className="mt-3 text-white/70">
          O Vigia de Posto mantém parte da experiência em cache. Reconecte para atualizar preços e relatos.
        </p>
        <Link href="/" className="mt-6 inline-flex rounded-md bg-limefuel px-5 py-3 font-bold text-ink">
          Ver dados em cache
        </Link>
      </section>
    </main>
  );
}
