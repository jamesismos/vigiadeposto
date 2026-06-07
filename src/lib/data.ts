import {
  Baby,
  BadgeCheck,
  Bath,
  Car,
  CircleDollarSign,
  Droplets,
  Fuel,
  Lightbulb,
  ShieldCheck,
  Siren,
  TrendingDown,
  TrendingUp,
  Users
} from "lucide-react";

export type StationStatus = "recommended" | "cheap" | "expensive" | "alert";

export interface StationCoordinate {
  lat: number;
  lng: number;
}

export const nationalStats = [
  { label: "postos monitorados", value: "42.860", detail: "+18 estados" },
  { label: "preços enviados hoje", value: "128.420", detail: "validados por reputação" },
  { label: "relatos moderados", value: "9.314", detail: "fraude, abuso e segurança" },
  { label: "índice confiável", value: "86%", detail: "baseado em recorrência" }
];

export const fuelAverages = [
  { fuel: "Gasolina", price: "R$ 5,82", delta: "+1,9%", state: "BR", trend: TrendingUp },
  { fuel: "Etanol", price: "R$ 3,74", delta: "-0,8%", state: "SP", trend: TrendingDown },
  { fuel: "Diesel S10", price: "R$ 6,11", delta: "+0,4%", state: "MG", trend: TrendingUp },
  { fuel: "GNV", price: "R$ 4,88", delta: "estável", state: "RJ", trend: CircleDollarSign }
];

export const stations = [
  {
    id: "posto-avenida-brasil",
    name: "Posto Avenida Brasil",
    flag: "Shell",
    city: "São Paulo, SP",
    address: "Av. Brasil, 2140 - Jardim América",
    lat: -23.5687,
    lng: -46.6803,
    score: 4.7,
    status: "recommended" as StationStatus,
    price: "R$ 5,69",
    fuel: "Gasolina",
    distance: "1,8 km",
    tags: ["seguro para mulheres", "banheiro bom", "preço justo"],
    comparison: "-2,2% vs média SP",
    nightSafe: true
  },
  {
    id: "posto-rodovia-116",
    name: "Auto Posto Rodovia 116",
    flag: "Ipiranga",
    city: "Curitiba, PR",
    address: "BR-116, km 102",
    lat: -25.4809,
    lng: -49.3044,
    score: 4.2,
    status: "cheap" as StationStatus,
    price: "R$ 3,61",
    fuel: "Etanol",
    distance: "8,4 km",
    tags: ["barato", "ducha", "estacionamento"],
    comparison: "-4,8% vs média PR",
    nightSafe: true
  },
  {
    id: "posto-centro-norte",
    name: "Posto Centro Norte",
    flag: "Bandeira branca",
    city: "Goiânia, GO",
    address: "Av. Anhanguera, 510",
    lat: -16.6809,
    lng: -49.2533,
    score: 2.8,
    status: "alert" as StationStatus,
    price: "R$ 6,42",
    fuel: "Gasolina",
    distance: "3,2 km",
    tags: ["relatos de falha", "preço acima", "moderação ativa"],
    comparison: "+9,6% vs média GO",
    nightSafe: false
  }
];

export const filters = ["gasolina", "etanol", "diesel", "gnv", "elétrico"];

export const ratingSignals = [
  { label: "combustível confiável", icon: Fuel },
  { label: "banheiro limpo", icon: Bath },
  { label: "atendimento", icon: Users },
  { label: "iluminação", icon: Lightbulb },
  { label: "segurança", icon: ShieldCheck },
  { label: "troca-fraldas", icon: Baby },
  { label: "acessibilidade", icon: BadgeCheck },
  { label: "local seguro à noite", icon: Car }
];

export const safetySignals = [
  "seguro para mulheres",
  "boa iluminação",
  "movimento noturno",
  "banheiro feminino limpo",
  "assédio relatado",
  "parada segura para viagem"
];

export const familySignals = [
  "troca-fraldas",
  "banheiro infantil",
  "acessibilidade",
  "conveniência",
  "área de descanso",
  "ducha/caminhoneiro",
  "estacionamento",
  "calibrador"
];

export const adminCards = [
  { label: "denúncias abertas", value: "312", icon: Siren, tone: "danger" },
  { label: "preços suspeitos", value: "1.204", icon: Droplets, tone: "warning" },
  { label: "revisões aprovadas", value: "27.901", icon: BadgeCheck, tone: "petrol" },
  { label: "postos em observação", value: "684", icon: ShieldCheck, tone: "civic" }
];

export const moderationQueue = [
  {
    title: "Possível combustível adulterado",
    location: "Posto Centro Norte - Goiânia",
    risk: "alto",
    evidence: "5 relatos semelhantes em 72h, nota de combustível caiu 31%"
  },
  {
    title: "Assédio relatado no estacionamento",
    location: "BR-101, km 87 - Recife",
    risk: "crítico",
    evidence: "relato anônimo com foto e horário; ocultar identidade publicamente"
  },
  {
    title: "Preço abusivo em feriado",
    location: "Posto Serra Azul - Dutra",
    risk: "médio",
    evidence: "diesel S10 14,2% acima da média regional e histórico local"
  }
];
