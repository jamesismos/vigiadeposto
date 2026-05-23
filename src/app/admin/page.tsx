import Link from "next/link";
import { ArrowLeft, BarChart3, CheckCircle2, Flame, Map, ShieldCheck } from "lucide-react";
import { adminCards, moderationQueue } from "@/lib/data";

const heatmapStates = [
  ["SP", 84],
  ["MG", 72],
  ["RJ", 68],
  ["PR", 62],
  ["BA", 51],
  ["GO", 49],
  ["PE", 43],
  ["RS", 39]
];

export default function AdminDashboard() {
  return (
    <main className="min-h-screen bg-[#f6f8fa] text-ink dark:bg-[#10151f] dark:text-white">
      <header className="border-b border-ink/10 bg-white dark:border-white/10 dark:bg-white/5">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-4">
          <Link href="/" className="inline-flex items-center gap-2 text-sm font-bold text-petrol dark:text-limefuel">
            <ArrowLeft size={18} /> Voltar
          </Link>
          <span className="rounded-md bg-ink px-3 py-2 text-sm font-bold text-white dark:bg-white dark:text-ink">
            Admin Vigia
          </span>
        </div>
      </header>

      <section className="mx-auto max-w-7xl px-5 py-10">
        <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-petrol dark:text-limefuel">
              dashboard admin
            </p>
            <h1 className="mt-3 text-4xl font-black">Moderação, abuso e inteligência nacional.</h1>
          </div>
          <button className="inline-flex items-center justify-center gap-2 rounded-md bg-petrol px-5 py-3 text-sm font-bold text-white">
            Exportar relatório <BarChart3 size={18} />
          </button>
        </div>

        <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {adminCards.map((card) => (
            <article key={card.label} className="rounded-lg border border-ink/10 bg-white p-5 shadow-sm dark:border-white/10 dark:bg-white/5">
              <card.icon className="h-7 w-7 text-petrol dark:text-limefuel" />
              <p className="mt-4 text-3xl font-black">{card.value}</p>
              <h2 className="mt-1 text-sm font-bold uppercase tracking-[0.12em] text-graphite dark:text-white/65">{card.label}</h2>
            </article>
          ))}
        </div>

        <div className="mt-8 grid gap-6 lg:grid-cols-[1.1fr_0.9fr]">
          <section className="rounded-lg border border-ink/10 bg-white p-6 shadow-soft dark:border-white/10 dark:bg-white/5">
            <div className="flex items-center gap-3">
              <Map className="h-6 w-6 text-petrol dark:text-limefuel" />
              <h2 className="text-2xl font-bold">Heatmap nacional</h2>
            </div>
            <div className="mt-6 grid gap-3">
              {heatmapStates.map(([state, value]) => (
                <div key={state} className="grid grid-cols-[44px_1fr_48px] items-center gap-3">
                  <span className="font-black">{state}</span>
                  <div className="h-4 overflow-hidden rounded-md bg-mist dark:bg-white/10">
                    <div className="h-full rounded-md bg-petrol dark:bg-limefuel" style={{ width: `${value}%` }} />
                  </div>
                  <span className="text-right text-sm font-bold">{value}%</span>
                </div>
              ))}
            </div>
          </section>

          <section className="rounded-lg border border-ink/10 bg-white p-6 shadow-soft dark:border-white/10 dark:bg-white/5">
            <div className="flex items-center gap-3">
              <Flame className="h-6 w-6 text-danger" />
              <h2 className="text-2xl font-bold">Fila de risco</h2>
            </div>
            <div className="mt-6 grid gap-4">
              {moderationQueue.map((item) => (
                <article key={item.title} className="rounded-lg border border-ink/10 p-4 dark:border-white/10">
                  <div className="flex items-start justify-between gap-3">
                    <h3 className="font-bold">{item.title}</h3>
                    <span className="rounded-md bg-danger/10 px-2 py-1 text-xs font-black uppercase text-danger">
                      {item.risk}
                    </span>
                  </div>
                  <p className="mt-2 text-sm font-semibold text-petrol dark:text-limefuel">{item.location}</p>
                  <p className="mt-2 text-sm leading-relaxed text-graphite dark:text-white/65">{item.evidence}</p>
                </article>
              ))}
            </div>
          </section>
        </div>

        <section className="mt-8 rounded-lg border border-ink/10 bg-white p-6 shadow-sm dark:border-white/10 dark:bg-white/5">
          <div className="flex items-center gap-3">
            <ShieldCheck className="h-6 w-6 text-petrol dark:text-limefuel" />
            <h2 className="text-2xl font-bold">Políticas operacionais</h2>
          </div>
          <div className="mt-5 grid gap-3 md:grid-cols-3">
            {["CAPTCHA invisível antes de denúncias", "Rate limiting por usuário, IP e posto", "Auditoria para admins e alterações sensíveis"].map((item) => (
              <div key={item} className="flex items-center gap-2 rounded-md bg-mist p-3 text-sm font-semibold dark:bg-white/10">
                <CheckCircle2 className="h-4 w-4 text-petrol dark:text-limefuel" />
                {item}
              </div>
            ))}
          </div>
        </section>
      </section>
    </main>
  );
}
