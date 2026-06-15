import Link from "next/link";
import {
  BarChart3,
  CheckCircle2,
  ChevronRight,
  FileWarning,
  LockKeyhole,
  MapPinned,
  Search,
  ShieldAlert,
  ShieldCheck,
  Sparkles,
  Star,
  TrendingDown,
  TrendingUp,
  UserRoundCheck
} from "lucide-react";
import { MapPreview } from "@/components/MapPreview";
import { PwaInstaller } from "@/components/PwaInstaller";
import { fetchStats, fetchFuelAverages, familyLabels, safetyLabels } from "@/lib/data";
import { calculateFairPriceIndex } from "@/lib/reputation";
import { createServerClient } from "@supabase/ssr";
import { cookies } from "next/headers";

export const dynamic = "force-dynamic";

export default async function Home() {
  const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL;
  const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY;

  if (!supabaseUrl || !supabaseAnonKey) {
    return (
      <main className="min-h-screen bg-[#f6f8fa] text-ink dark:bg-[#10151f] dark:text-white grid place-items-center px-5">
        <section className="max-w-md rounded-lg border border-ink/10 bg-white p-8 text-center dark:border-white/10 dark:bg-white/5 shadow-soft">
          <span className="mx-auto flex h-12 w-12 items-center justify-center rounded-lg bg-petrol text-white mb-4">
            <MapPinned size={26} />
          </span>
          <h1 className="text-2xl font-black">Vigia de Posto</h1>
          <p className="mt-4 text-graphite dark:text-white/80 leading-relaxed">
            Estamos preparando a plataforma para você. O Vigia de Posto estará no ar em instantes!
          </p>
          <div className="mt-6 h-1 w-12 bg-petrol mx-auto rounded" />
        </section>
      </main>
    );
  }

  const cookieStore = await cookies();
  const supabase = createServerClient(
    supabaseUrl,
    supabaseAnonKey,
    {
      cookies: {
        getAll: () => cookieStore.getAll(),
        setAll: () => {},
      },
    }
  );

  const [{ data: stations }, stats, prices] = await Promise.all([
    supabase
      .from("fuel_stations")
      .select("*")
      .eq("status", "active")
      .order("reputation_score", { ascending: false })
      .limit(6),
    fetchStats(),
    fetchFuelAverages(),
  ]);

  const stationList = stations || [];
  const hasData = stationList.length > 0;

  const firstStation = stationList[0];
  const fairIndex = firstStation
    ? calculateFairPriceIndex({
        userPrice: firstStation.reputation_score || 0,
        stateAverage: 5.82,
        anpAverage: 5.88,
        stationScore: firstStation.reputation_score || 0,
        complaintRate: firstStation.complaint_index || 0,
      })
    : null;

  return (
    <main className="min-h-screen bg-[#f6f8fa] text-ink dark:bg-[#10151f] dark:text-white">
      <header className="sticky top-0 z-30 border-b border-ink/10 bg-white/88 backdrop-blur dark:border-white/10 dark:bg-[#10151f]/88">
        <nav className="mx-auto flex max-w-7xl items-center justify-between px-5 py-4">
          <Link href="/" className="flex items-center gap-3">
            <span className="flex h-10 w-10 items-center justify-center rounded-lg bg-petrol text-white">
              <MapPinned size={22} />
            </span>
            <span>
              <strong className="block text-base">Vigia de Posto</strong>
              <span className="text-xs font-semibold uppercase tracking-[0.16em] text-petrol dark:text-limefuel">
                by JAMESYSTEM
              </span>
            </span>
          </Link>
          <div className="hidden items-center gap-6 text-sm font-semibold text-graphite dark:text-white/75 md:flex">
            <a href="#mapa">Mapa</a>
            <a href="#relatar">Relatar</a>
            <a href="#seguranca">Segurança</a>
            <a href="#ajude">Ajude</a>
            <Link href="/admin" className="rounded-md bg-ink px-3 py-2 text-white dark:bg-white dark:text-ink">
              Admin
            </Link>
          </div>
        </nav>
      </header>

      <section className="relative overflow-hidden bg-ink text-white">
        <div className="absolute inset-0 bg-[linear-gradient(120deg,rgba(7,95,104,0.92),rgba(16,21,31,0.94))] bg-cover bg-center" />
        <div className="relative mx-auto grid max-w-7xl gap-8 px-5 pb-12 pt-16 lg:grid-cols-[1.05fr_0.95fr] lg:pb-16">
          <div className="flex min-h-[560px] flex-col justify-center">
            <div className="mb-5 inline-flex w-fit items-center gap-2 rounded-md border border-white/15 bg-white/10 px-3 py-2 text-sm font-semibold backdrop-blur">
              <Sparkles size={16} className="text-limefuel" />
              Seguranca, estrutura e preco em todo o Brasil
            </div>
            <h1 className="max-w-4xl text-4xl font-black leading-tight md:text-6xl">
              Vigia de Posto
            </h1>
            <p className="mt-5 max-w-2xl text-lg leading-relaxed text-white/82">
              Avalie o lugar: tem banheiro limpo? E seguro para mulheres? Tem trocador? Iluminacao? Envie precos tambem. Sem rede social.
            </p>
            <div className="mt-8 grid max-w-2xl gap-3 rounded-lg border border-white/15 bg-white/10 p-2 backdrop-blur sm:grid-cols-[1fr_auto]">
              <label className="flex items-center gap-3 rounded-md bg-white px-4 py-3 text-ink">
                <Search size={20} className="text-petrol" />
                <input
                  className="w-full bg-transparent text-sm outline-none"
                  placeholder="Buscar cidade, bairro, rodovia ou posto"
                />
              </label>
              <a href="#mapa" className="inline-flex items-center justify-center gap-2 rounded-md bg-limefuel px-5 py-3 text-sm font-bold text-ink">
                Abrir mapa <ChevronRight size={18} />
              </a>
            </div>
          </div>
          <div className="grid content-center gap-4">
            <PwaInstaller />
            <div className="rounded-lg border border-white/15 bg-white/10 p-5 shadow-soft backdrop-blur">
              <p className="text-sm font-semibold uppercase tracking-[0.16em] text-limefuel">preço médio hoje</p>
              {prices.length > 0 ? (
                <div className="mt-4 grid gap-3 sm:grid-cols-2">
                  {prices.slice(0, 4).map((item: any) => (
                    <div key={item.fuel} className="rounded-lg bg-white/10 p-4">
                      <div className="flex items-center justify-between gap-2">
                        <span className="font-bold">{item.fuel}</span>
                        <TrendingUp className="h-4 w-4 text-limefuel" />
                      </div>
                      <p className="mt-3 text-2xl font-black">R$ {item.average_price}</p>
                      <p className="mt-1 text-sm text-white/70">{item.trend} · {item.state}</p>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="mt-4 text-sm text-white/70">
                  Ainda sem dados suficientes. Envie um preço para começar.
                </p>
              )}
            </div>
          </div>
        </div>
      </section>

      <section className="mx-auto grid max-w-7xl gap-4 px-5 py-10 sm:grid-cols-2 lg:grid-cols-4">
        {[
          { label: "postos monitorados", value: stats.stations.toString(), detail: "dados reais da comunidade" },
          { label: "preços enviados hoje", value: stats.prices.toString(), detail: "validados por reputação" },
          { label: "relatos moderados", value: stats.reports.toString(), detail: "fraude, abuso e segurança" },
          { label: "índice confiável", value: stats.stations > 0 ? `${Math.round((stats.reports / Math.max(stats.stations, 1)) * 100)}%` : "0%", detail: "baseado em recorrência real" },
        ].map((stat) => (
          <article key={stat.label} className="rounded-lg border border-ink/10 bg-white p-5 shadow-sm dark:border-white/10 dark:bg-white/5">
            <p className="text-3xl font-black">{stat.value}</p>
            <h2 className="mt-2 text-sm font-bold uppercase tracking-[0.12em] text-petrol dark:text-limefuel">{stat.label}</h2>
            <p className="mt-2 text-sm text-graphite dark:text-white/60">{stat.detail}</p>
          </article>
        ))}
      </section>

      <MapPreview />

      <section className="mx-auto grid max-w-7xl gap-8 px-5 py-16 lg:grid-cols-[1fr_1fr]">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-petrol dark:text-limefuel">
            perfil do posto
          </p>
          <h2 className="mt-3 text-3xl font-bold md:text-4xl">Reputacao, preco e historico.</h2>
          {hasData ? (
            <div className="mt-6 rounded-lg border border-ink/10 bg-white p-5 shadow-soft dark:border-white/10 dark:bg-white/5">
              <div className="flex flex-wrap items-start justify-between gap-4">
                <div>
                  <h3 className="text-xl font-black">{firstStation.name}</h3>
                  <p className="mt-1 text-sm text-graphite dark:text-white/60">{firstStation.flag} · {firstStation.address}</p>
                </div>
                <span className="rounded-md bg-limefuel px-3 py-2 text-sm font-black text-ink">
                  {fairIndex?.label ?? "Sem índice"} · {fairIndex ? Math.round(fairIndex.score) : 0}
                </span>
              </div>
              <div className="mt-5 grid gap-3 sm:grid-cols-3">
                <div className="rounded-lg bg-mist p-4 font-bold dark:bg-white/10">Nota: {firstStation.reputation_score ?? 0}</div>
                <div className="rounded-lg bg-mist p-4 font-bold dark:bg-white/10">Segurança: {firstStation.safety_score ?? 0}</div>
                <div className="rounded-lg bg-mist p-4 font-bold dark:bg-white/10">Família: {firstStation.family_score ?? 0}</div>
              </div>
              <div className="mt-5 grid gap-3 sm:grid-cols-2">
                {["combustível confiável", "banheiro limpo", "atendimento", "iluminação", "segurança", "troca-fraldas", "acessibilidade", "local seguro à noite"].map((label) => (
                  <div key={label} className="flex items-center gap-3 rounded-md border border-ink/10 p-3 text-sm font-semibold dark:border-white/10">
                    <CheckCircle2 className="h-4 w-4 text-petrol dark:text-limefuel" />
                    {label}
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="mt-6 rounded-lg border border-ink/10 bg-white p-5 shadow-soft dark:border-white/10 dark:bg-white/5">
              <p className="text-graphite dark:text-white/60">
                Ainda não há postos avaliados. Cadastre o primeiro posto e envie uma avaliação estruturada.
              </p>
            </div>
          )}
        </div>
        <div id="relatar" className="rounded-lg border border-ink/10 bg-white p-5 shadow-soft dark:border-white/10 dark:bg-white/5">
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-petrol dark:text-limefuel">
            relatar experiência
          </p>
          <h2 className="mt-3 text-2xl font-bold">Denúncia, elogio ou preço em menos de um minuto.</h2>
          <form className="mt-6 grid gap-4">
            <select className="rounded-md border border-ink/10 bg-white px-4 py-3 dark:border-white/10 dark:bg-[#10151f]">
              <option>denúncia</option>
              <option>elogio</option>
              <option>informação</option>
              <option>atualização de preço</option>
            </select>
            <div className="grid gap-4 sm:grid-cols-2">
              <input className="rounded-md border border-ink/10 px-4 py-3 dark:border-white/10 dark:bg-[#10151f]" placeholder="Combustível" />
              <input className="rounded-md border border-ink/10 px-4 py-3 dark:border-white/10 dark:bg-[#10151f]" placeholder="Valor pago" />
            </div>
            <textarea className="min-h-28 rounded-md border border-ink/10 px-4 py-3 dark:border-white/10 dark:bg-[#10151f]" placeholder="Comentário público, sem dados pessoais" />
            <label className="flex items-center gap-3 rounded-md border border-ink/10 p-3 text-sm font-semibold dark:border-white/10">
              <input type="checkbox" />
              Publicar como anônimo
            </label>
            <button className="inline-flex items-center justify-center gap-2 rounded-md bg-petrol px-5 py-3 font-bold text-white">
              Enviar para moderação <ShieldCheck size={18} />
            </button>
            <p className="text-xs leading-relaxed text-graphite dark:text-white/60">
              Mesmo logado, a identidade anônima não aparece publicamente. Metadados ficam restritos a auditoria LGPD e abuso.
            </p>
          </form>
        </div>
      </section>

      <section id="seguranca" className="bg-white py-16 dark:bg-white/5">
        <div className="mx-auto grid max-w-7xl gap-8 px-5 lg:grid-cols-2">
          <SignalPanel title="Segurança feminina" icon={UserRoundCheck} items={safetyLabels} />
          <SignalPanel title="Família e estrutura" icon={Star} items={familyLabels} />
        </div>
      </section>

      <section className="mx-auto grid max-w-7xl gap-6 px-5 py-16 lg:grid-cols-3">
        {[
          ["LGPD e RLS", "Políticas por tabela no Supabase, anonimização pública e logs de auditoria.", LockKeyhole],
          ["Moderação automática", "Fila por risco, abuso, spam, evidências e recorrência regional.", ShieldAlert],
          ["Dados públicos", "Preparado para ANP, Petrobras, ICMS estadual e séries históricas.", BarChart3]
        ].map(([title, text, Icon]) => (
          <article key={title as string} className="rounded-lg border border-ink/10 bg-white p-6 shadow-sm dark:border-white/10 dark:bg-white/5">
            <Icon className="h-8 w-8 text-petrol dark:text-limefuel" />
            <h2 className="mt-4 text-xl font-bold">{title as string}</h2>
            <p className="mt-3 leading-relaxed text-graphite dark:text-white/65">{text as string}</p>
          </article>
        ))}
      </section>

      <section id="ajude" className="bg-ink py-16 text-white">
        <div className="mx-auto grid max-w-7xl gap-8 px-5 lg:grid-cols-[1fr_0.8fr]">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-limefuel">ajude o projeto</p>
            <h2 className="mt-3 text-3xl font-bold md:text-4xl">Ajude a manter o app no ar.</h2>
            <p className="mt-4 max-w-2xl leading-relaxed text-white/72">
              Infraestrutura, dados, moderacao e evolucao do app sem comprometer a experiencia.
            </p>
            <div className="mt-6 grid gap-3">
              <DonationLine label="PIX" value="c23396a1-9c0e-4795-919e-d48e528074f2" />
              <DonationLine label="BTC" value="bc1qzewr447fjwln66es26qdzkwmpqy3ukfvs89nnz" />
            </div>
          </div>
          <div className="rounded-lg bg-white p-5 text-ink shadow-soft">
            <div className="mx-auto h-56 w-56 rounded-lg border-8 border-white bg-white p-3 shadow-inner">
              <div className="qr-grid h-full w-full rounded-md" />
            </div>
            <p className="mt-4 text-center text-sm font-bold">QR elegante para PIX/BTC</p>
          </div>
        </div>
      </section>

      <footer className="border-t border-ink/10 bg-white px-5 py-8 text-sm text-graphite dark:border-white/10 dark:bg-[#10151f] dark:text-white/60">
        <div className="mx-auto flex max-w-7xl flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <p>Vigia de Posto · WebApp/PWA brasileiro de fiscalização colaborativa.</p>
          <p>AdSense preparado: ca-pub-3090285265842642</p>
        </div>
      </footer>
    </main>
  );
}

function SignalPanel({ title, icon: Icon, items }: { title: string; icon: typeof TrendingDown; items: string[] }) {
  return (
    <article className="rounded-lg border border-ink/10 bg-[#f6f8fa] p-6 shadow-sm dark:border-white/10 dark:bg-[#10151f]">
      <div className="flex items-center gap-3">
        <span className="flex h-11 w-11 items-center justify-center rounded-lg bg-petrol text-white">
          <Icon size={22} />
        </span>
        <h2 className="text-2xl font-bold">{title}</h2>
      </div>
      <div className="mt-5 grid gap-3 sm:grid-cols-2">
        {items.map((item) => (
          <div key={item} className="flex items-center gap-2 rounded-md bg-white p-3 text-sm font-semibold dark:bg-white/10">
            <CheckCircle2 className="h-4 w-4 text-petrol dark:text-limefuel" />
            {item}
          </div>
        ))}
      </div>
    </article>
  );
}

function DonationLine({ label, value }: { label: string; value: string }) {
  return (
    <div className="rounded-lg border border-white/15 bg-white/8 p-4">
      <p className="text-xs font-bold uppercase tracking-[0.16em] text-limefuel">{label}</p>
      <p className="mt-2 break-all font-mono text-sm text-white/85">{value}</p>
    </div>
  );
}
