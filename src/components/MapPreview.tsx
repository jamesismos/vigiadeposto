"use client";

import { useState } from "react";
import { LocateFixed, MapPin, ShieldCheck } from "lucide-react";
import { filters, stations } from "@/lib/data";

const markerPosition = [
  "left-[20%] top-[34%]",
  "left-[58%] top-[22%]",
  "left-[71%] top-[58%]"
];

export function MapPreview() {
  const [activeFilter, setActiveFilter] = useState("gasolina");

  return (
    <section id="mapa" className="bg-[#f6f8fa] py-16 text-ink dark:bg-[#10151f] dark:text-white">
      <div className="mx-auto grid max-w-7xl gap-8 px-5 lg:grid-cols-[0.85fr_1.15fr]">
        <div className="space-y-6">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-petrol dark:text-limefuel">
              mapa colaborativo
            </p>
            <h2 className="mt-3 text-3xl font-bold md:text-4xl">Postos, preços e riscos no mesmo painel.</h2>
            <p className="mt-4 text-base leading-relaxed text-graphite dark:text-white/70">
              Preparado para Mapbox com clusterização, heatmap, busca por cidade, bairro e rodovia.
            </p>
          </div>
          <div className="flex flex-wrap gap-2">
            {filters.map((filter) => (
              <button
                key={filter}
                className={`rounded-md border px-3 py-2 text-sm font-semibold capitalize transition ${
                  activeFilter === filter
                    ? "border-petrol bg-petrol text-white"
                    : "border-ink/10 bg-white text-graphite hover:border-petrol/50 dark:border-white/10 dark:bg-white/5 dark:text-white/80"
                }`}
                onClick={() => setActiveFilter(filter)}
              >
                {filter}
              </button>
            ))}
          </div>
          <div className="grid gap-3">
            {stations.map((station) => (
              <article key={station.id} className="rounded-lg border border-ink/10 bg-white p-4 shadow-sm dark:border-white/10 dark:bg-white/5">
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <h3 className="font-bold">{station.name}</h3>
                    <p className="mt-1 text-sm text-graphite dark:text-white/60">{station.city} · {station.distance}</p>
                  </div>
                  <span className="rounded-md bg-limefuel/25 px-2 py-1 text-sm font-bold text-petrol dark:text-limefuel">{station.score}</span>
                </div>
                <div className="mt-3 flex flex-wrap gap-2">
                  {station.tags.map((tag) => (
                    <span key={tag} className="rounded-md bg-mist px-2 py-1 text-xs font-semibold text-graphite dark:bg-white/10 dark:text-white/75">
                      {tag}
                    </span>
                  ))}
                </div>
              </article>
            ))}
          </div>
        </div>
        <div className="relative min-h-[520px] overflow-hidden rounded-lg border border-ink/10 bg-[#dce8e6] shadow-soft dark:border-white/10 dark:bg-[#18222b]">
          <div className="absolute inset-0 bg-[linear-gradient(90deg,rgba(7,95,104,0.12)_1px,transparent_1px),linear-gradient(rgba(7,95,104,0.12)_1px,transparent_1px)] bg-[size:48px_48px]" />
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_35%_35%,rgba(167,201,87,0.35),transparent_20%),radial-gradient(circle_at_70%_62%,rgba(214,69,69,0.24),transparent_17%)]" />
          <div className="absolute left-6 top-6 flex items-center gap-2 rounded-md bg-white/90 px-3 py-2 text-sm font-bold text-ink shadow-soft">
            <LocateFixed size={16} />
            São Paulo · {activeFilter}
          </div>
          {stations.map((station, index) => (
            <div key={station.id} className={`absolute ${markerPosition[index]} w-56 rounded-lg bg-white p-3 shadow-soft dark:bg-[#111923]`}>
              <div className="flex items-center gap-2">
                <span className={`flex h-9 w-9 items-center justify-center rounded-md text-white ${
                  station.status === "alert" ? "bg-danger" : station.status === "cheap" ? "bg-petrol" : "bg-limefuel text-ink"
                }`}>
                  <MapPin size={18} />
                </span>
                <div>
                  <p className="text-sm font-bold text-ink dark:text-white">{station.price}</p>
                  <p className="text-xs text-graphite dark:text-white/60">{station.comparison}</p>
                </div>
              </div>
            </div>
          ))}
          <div className="absolute bottom-5 left-5 right-5 grid gap-3 rounded-lg bg-white/92 p-4 shadow-soft backdrop-blur dark:bg-[#111923]/92 sm:grid-cols-3">
            {["barato", "recomendado", "suspeito"].map((item) => (
              <div key={item} className="flex items-center gap-2 text-sm font-semibold text-ink dark:text-white">
                <ShieldCheck className="h-4 w-4 text-petrol dark:text-limefuel" />
                {item}
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
