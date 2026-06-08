import { createClient } from "@supabase/supabase-js";

const supabase = createClient(
  process.env.NEXT_PUBLIC_SUPABASE_URL!,
  process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!
);

export type StationStatus = "recommended" | "cheap" | "expensive" | "alert";

export interface Station {
  id: string;
  name: string;
  flag: string | null;
  address: string;
  city: string;
  state: string;
  lat: number;
  lng: number;
  reputation_score: number;
  safety_score: number;
  family_score: number;
  complaint_index: number;
  status: string;
}

export const filters = ["gasolina", "etanol", "diesel", "gnv", "elétrico"];

/** Busca postos reais do Supabase */
export async function fetchStations(limit = 20) {
  const { data, error } = await supabase
    .from("fuel_stations")
    .select("*")
    .eq("status", "active")
    .order("reputation_score", { ascending: false })
    .limit(limit);
  if (error) throw error;
  return (data || []) as Station[];
}

/** Contagem real: postos, preços, relatos */
export async function fetchStats() {
  const { count: stationCount } = await supabase
    .from("fuel_stations")
    .select("*", { count: "exact", head: true })
    .eq("status", "active");

  const { count: priceCount } = await supabase
    .from("fuel_prices")
    .select("*", { count: "exact", head: true });

  const { count: reportCount } = await supabase
    .from("reports")
    .select("*", { count: "exact", head: true });

  return {
    stations: stationCount || 0,
    prices: priceCount || 0,
    reports: reportCount || 0,
  };
}

/** Média de preço por combustível (dados reais) */
export async function fetchFuelAverages() {
  const { data, error } = await supabase
    .from("regional_fuel_indicators")
    .select("fuel, average_price, trend, state")
    .order("collected_at", { ascending: false })
    .limit(10);
  if (error) throw error;
  return data || [];
}

/** Busca reputação Google Places (opcional, requer Places API key) */
export async function fetchGoogleReviews(placeId: string) {
  const key = process.env.GOOGLE_PLACES_API_KEY;
  if (!key) return { enabled: false, reviews: [] };
  const res = await fetch(
    `https://maps.googleapis.com/maps/api/place/details/json?place_id=${placeId}&fields=rating,reviews,user_ratings_total&key=${key}&language=pt-BR`
  );
  const json = await res.json();
  return {
    enabled: true,
    rating: json.result?.rating,
    total: json.result?.user_ratings_total,
    reviews: json.result?.reviews || [],
  };
}

/** Indicadores de avaliação (labels apenas — dados vêm do banco) */
export const ratingLabels = [
  "combustível confiável",
  "banheiro limpo",
  "atendimento",
  "iluminação",
  "segurança",
  "troca-fraldas",
  "acessibilidade",
  "local seguro à noite",
];

export const safetyLabels = [
  "seguro para mulheres",
  "boa iluminação",
  "movimento noturno",
  "banheiro feminino limpo",
  "assédio relatado",
  "parada segura para viagem",
];

export const familyLabels = [
  "troca-fraldas",
  "banheiro infantil",
  "acessibilidade",
  "conveniência",
  "área de descanso",
  "ducha/caminhoneiro",
  "estacionamento",
  "calibrador",
];
