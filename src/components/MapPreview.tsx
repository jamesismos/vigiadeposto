"use client";

import { useEffect, useRef, useState, useCallback } from "react";
import maplibregl from "maplibre-gl";
import "maplibre-gl/dist/maplibre-gl.css";
import { LocateFixed, ShieldCheck } from "lucide-react";
import { filters } from "@/lib/data";
import { createBrowserClient } from "@supabase/ssr";

function createClient() {
  return createBrowserClient(
    process.env.NEXT_PUBLIC_SUPABASE_URL!,
    process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!
  );
}

interface Station {
  id: string;
  name: string;
  city: string;
  state: string;
  lat: number;
  lng: number;
  reputation_score: number;
  safety_score: number;
  family_score: number;
  status: string;
}

export function MapPreview() {
  const container = useRef<HTMLDivElement>(null);
  const map = useRef<maplibregl.Map | null>(null);
  const markers = useRef<maplibregl.Marker[]>([]);
  const [activeFilter, setActiveFilter] = useState("gasolina");
  const [ready, setReady] = useState(false);
  const [stations, setStations] = useState<Station[]>([]);
  const [loading, setLoading] = useState(true);

  const supabase = createClient();

  const fly = useCallback((lat: number, lng: number) => {
    map.current?.flyTo({ center: [lng, lat], zoom: 13, duration: 1200 });
  }, []);

  useEffect(() => {
    async function fetchStations() {
      const { data } = await supabase
        .from("fuel_stations")
        .select("*")
        .eq("status", "active")
        .order("reputation_score", { ascending: false })
        .limit(20);
      setStations(data || []);
      setLoading(false);
    }
    fetchStations();
  }, [supabase]);

  useEffect(() => {
    if (!container.current || map.current) return;
    const m = new maplibregl.Map({
      container: container.current,
      attributionControl: { compact: true },
      style: {
        version: 8,
        sources: {
          osm: {
            type: "raster",
            tiles: ["https://tile.openstreetmap.org/{z}/{x}/{y}.png"],
            tileSize: 256,
            attribution: "&copy; OpenStreetMap contributors",
          },
        },
        layers: [{ id: "osm", type: "raster", source: "osm" }],
      },
      center: [-49, -18.5],
      zoom: 4.5,
    });
    m.on("load", () => setReady(true));
    map.current = m;
    return () => {
      markers.current.forEach((mk) => mk.remove());
      markers.current = [];
      m.remove();
      map.current = null;
    };
  }, []);

  useEffect(() => {
    if (!map.current || !ready) return;
    markers.current.forEach((mk) => mk.remove());
    markers.current = [];
    stations.forEach((s) => {
      if (s.lat === undefined || s.lng === undefined) return;
      const el = document.createElement("div");
      el.title = s.name;
      const bg = s.reputation_score < 3 ? "#dc2626" : s.reputation_score < 4 ? "#076058" : "#a7c957";
      const price = "R$ --"; // preço real virá de fuel_prices
      el.innerHTML = `<div style="
        background:${bg};color:white;padding:2px 10px;border-radius:999px;
        font-weight:700;font-size:11px;white-space:nowrap;cursor:pointer;
        box-shadow:0 2px 8px rgba(0,0,0,0.25);border:2px solid white;
        transform:translate(-50%,-100%)
      ">${price}</div>`;
      el.addEventListener("click", () => fly(s.lat, s.lng));
      const marker = new maplibregl.Marker({ element: el })
        .setLngLat([s.lng, s.lat])
        .addTo(map.current!);
      markers.current.push(marker);
    });
    if (stations.length > 0) {
      const bounds = stations.reduce(
        (b, s) => b.extend([s.lng, s.lat]),
        new maplibregl.LngLatBounds([stations[0].lng, stations[0].lat], [stations[0].lng, stations[0].lat])
      );
      map.current.fitBounds(bounds, { padding: 50, duration: 1000 });
    }
  }, [ready, stations, fly]);

  return (
    <section id="mapa" className="bg-[#f6f8fa] py-16 text-ink dark:bg-[#10151f] dark:text-white">
      <div className="mx-auto grid max-w-7xl gap-8 px-5 lg:grid-cols-[0.85fr_1.15fr]">
        <div className="space-y-6">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.18em] text-petrol dark:text-limefuel">
              mapa
            </p>
            <h2 className="mt-3 text-3xl font-bold md:text-4xl">Postos, preços e riscos no mesmo painel.</h2>
            <p className="mt-4 text-base leading-relaxed text-graphite dark:text-white/70">
              Mapa gratuito com OpenStreetMap + MapLibre. Sem custo por visualizacao.
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
            {loading ? (
              <p className="text-sm text-graphite dark:text-white/60">Carregando postos...</p>
            ) : stations.length === 0 ? (
              <p className="text-sm text-graphite dark:text-white/60">
                Nenhum posto cadastrado ainda. Cadastre o primeiro.
              </p>
            ) : (
              stations.map((s) => (
                <article
                  key={s.id}
                  onClick={() => fly(s.lat, s.lng)}
                  className="cursor-pointer rounded-lg border border-ink/10 bg-white p-4 shadow-sm transition hover:border-petrol/50 dark:border-white/10 dark:bg-white/5"
                >
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <h3 className="font-bold">{s.name}</h3>
                      <p className="mt-1 text-sm text-graphite dark:text-white/60">{s.city}, {s.state}</p>
                    </div>
                    <span className="rounded-md bg-limefuel/25 px-2 py-1 text-sm font-bold text-petrol dark:text-limefuel">
                      {s.reputation_score?.toFixed(1) ?? "0"}
                    </span>
                  </div>
                  <div className="mt-3 flex flex-wrap gap-2">
                    {s.safety_score > 0 && (
                      <span className="rounded-md bg-mist px-2 py-1 text-xs font-semibold text-graphite dark:bg-white/10 dark:text-white/75">
                        Segurança: {s.safety_score.toFixed(1)}
                      </span>
                    )}
                    {s.family_score > 0 && (
                      <span className="rounded-md bg-mist px-2 py-1 text-xs font-semibold text-graphite dark:bg-white/10 dark:text-white/75">
                        Família: {s.family_score.toFixed(1)}
                      </span>
                    )}
                  </div>
                </article>
              ))
            )}
          </div>
        </div>
        <div className="relative min-h-[520px] overflow-hidden rounded-lg border border-ink/10 shadow-soft dark:border-white/10">
          <div ref={container} className="absolute inset-0" />
          <div className="absolute left-4 top-4 z-10 flex items-center gap-2 rounded-md bg-white/90 px-3 py-2 text-sm font-bold text-ink shadow-soft">
            <LocateFixed size={16} />
            Brasil · {activeFilter}
          </div>
          <div className="absolute bottom-4 left-4 right-4 z-10 grid gap-3 rounded-lg bg-white/92 p-4 shadow-soft backdrop-blur dark:bg-[#111923]/92 sm:grid-cols-3">
            {["barato", "recomendado", "suspeito"].map((item) => (
              <div key={item} className="flex items-center gap-2 text-sm font-semibold text-ink dark:text-white">
                <ShieldCheck className="h-4 w-4 text-petrol dark:text-limefuel" />
                {item}
              </div>
            ))}
          </div>
          <div className="absolute bottom-2 right-3 z-10 text-[10px] text-white/50">&copy; OpenStreetMap contributors</div>
        </div>
      </div>
    </section>
  );
}
