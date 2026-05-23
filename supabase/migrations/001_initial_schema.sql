create extension if not exists "uuid-ossp";
create extension if not exists "pgcrypto";

create type public.fuel_type as enum ('gasoline', 'ethanol', 'diesel', 'diesel_s10', 'gnv', 'electric');
create type public.report_type as enum ('complaint', 'praise', 'information', 'price_update');
create type public.moderation_status as enum ('pending', 'approved', 'rejected', 'needs_review');
create type public.risk_level as enum ('low', 'medium', 'high', 'critical');

create table public.users (
  id uuid primary key references auth.users(id) on delete cascade,
  display_name text,
  avatar_url text,
  city text,
  state char(2),
  reputation_score integer not null default 10,
  anonymous_default boolean not null default false,
  lgpd_consent_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table public.admins (
  user_id uuid primary key references public.users(id) on delete cascade,
  role text not null check (role in ('moderator', 'analyst', 'super_admin')),
  created_at timestamptz not null default now()
);

create table public.fuel_stations (
  id uuid primary key default uuid_generate_v4(),
  name text not null,
  flag text,
  cnpj text,
  address text not null,
  city text not null,
  state char(2) not null,
  neighborhood text,
  highway text,
  latitude numeric(10, 7) not null,
  longitude numeric(10, 7) not null,
  opening_hours jsonb not null default '{}'::jsonb,
  amenities jsonb not null default '{}'::jsonb,
  reputation_score numeric(4, 2) not null default 0,
  safety_score numeric(4, 2) not null default 0,
  family_score numeric(4, 2) not null default 0,
  complaint_index numeric(6, 4) not null default 0,
  status text not null default 'active' check (status in ('active', 'under_review', 'hidden')),
  created_by uuid references public.users(id),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index fuel_stations_geo_idx on public.fuel_stations (state, city, latitude, longitude);
create index fuel_stations_search_idx on public.fuel_stations using gin (to_tsvector('portuguese', name || ' ' || city || ' ' || coalesce(neighborhood, '') || ' ' || coalesce(highway, '')));

create table public.fuel_prices (
  id uuid primary key default uuid_generate_v4(),
  station_id uuid not null references public.fuel_stations(id) on delete cascade,
  user_id uuid references public.users(id) on delete set null,
  fuel public.fuel_type not null,
  price numeric(6, 3) not null check (price > 0 and price < 30),
  state_average numeric(6, 3),
  anp_average numeric(6, 3),
  icms_rate numeric(5, 2),
  source text not null default 'user' check (source in ('user', 'anp', 'petrobras', 'state_feed', 'admin')),
  is_anonymous boolean not null default false,
  evidence_url text,
  confidence_score numeric(4, 2) not null default 0.5,
  created_at timestamptz not null default now()
);

create index fuel_prices_station_fuel_created_idx on public.fuel_prices (station_id, fuel, created_at desc);

create table public.ratings (
  id uuid primary key default uuid_generate_v4(),
  station_id uuid not null references public.fuel_stations(id) on delete cascade,
  user_id uuid references public.users(id) on delete set null,
  fuel_quality smallint check (fuel_quality between 1 and 5),
  bathroom_cleanliness smallint check (bathroom_cleanliness between 1 and 5),
  service smallint check (service between 1 and 5),
  lighting smallint check (lighting between 1 and 5),
  safety smallint check (safety between 1 and 5),
  women_safety smallint check (women_safety between 1 and 5),
  accessibility smallint check (accessibility between 1 and 5),
  family_structure smallint check (family_structure between 1 and 5),
  overall smallint not null check (overall between 1 and 5),
  is_anonymous boolean not null default false,
  created_at timestamptz not null default now()
);

create table public.reviews (
  id uuid primary key default uuid_generate_v4(),
  station_id uuid not null references public.fuel_stations(id) on delete cascade,
  user_id uuid references public.users(id) on delete set null,
  rating_id uuid references public.ratings(id) on delete set null,
  title text,
  comment text not null,
  is_anonymous boolean not null default false,
  status public.moderation_status not null default 'pending',
  created_at timestamptz not null default now()
);

create table public.reports (
  id uuid primary key default uuid_generate_v4(),
  station_id uuid not null references public.fuel_stations(id) on delete cascade,
  user_id uuid references public.users(id) on delete set null,
  type public.report_type not null,
  fuel public.fuel_type,
  paid_price numeric(6, 3),
  comment text,
  image_url text,
  risk public.risk_level not null default 'low',
  is_anonymous boolean not null default false,
  public_author_label text generated always as (case when is_anonymous then 'Usuário anônimo' else null end) stored,
  status public.moderation_status not null default 'pending',
  created_at timestamptz not null default now()
);

create table public.moderation_logs (
  id uuid primary key default uuid_generate_v4(),
  admin_id uuid references public.admins(user_id) on delete set null,
  entity_type text not null,
  entity_id uuid not null,
  action text not null,
  reason text,
  metadata jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now()
);

create table public.favorites (
  user_id uuid not null references public.users(id) on delete cascade,
  station_id uuid not null references public.fuel_stations(id) on delete cascade,
  created_at timestamptz not null default now(),
  primary key (user_id, station_id)
);

create table public.regional_fuel_indicators (
  id uuid primary key default uuid_generate_v4(),
  state char(2) not null,
  city text,
  fuel public.fuel_type not null,
  average_price numeric(6, 3) not null,
  anp_average numeric(6, 3),
  petrobras_reference numeric(6, 3),
  icms_rate numeric(5, 2),
  trend text check (trend in ('up', 'down', 'stable')),
  collected_at timestamptz not null default now()
);

create or replace function public.is_admin()
returns boolean
language sql
security definer
set search_path = public
as $$
  select exists (select 1 from public.admins where user_id = auth.uid());
$$;

alter table public.users enable row level security;
alter table public.admins enable row level security;
alter table public.fuel_stations enable row level security;
alter table public.fuel_prices enable row level security;
alter table public.ratings enable row level security;
alter table public.reviews enable row level security;
alter table public.reports enable row level security;
alter table public.moderation_logs enable row level security;
alter table public.favorites enable row level security;
alter table public.regional_fuel_indicators enable row level security;

create policy "public stations are readable" on public.fuel_stations for select using (status <> 'hidden');
create policy "authenticated users create stations" on public.fuel_stations for insert with check (auth.uid() = created_by);
create policy "admins manage stations" on public.fuel_stations for all using (public.is_admin()) with check (public.is_admin());

create policy "public approved prices are readable" on public.fuel_prices for select using (true);
create policy "authenticated users create prices" on public.fuel_prices for insert with check (auth.uid() = user_id);
create policy "admins manage prices" on public.fuel_prices for all using (public.is_admin()) with check (public.is_admin());

create policy "public ratings are readable" on public.ratings for select using (true);
create policy "authenticated users create ratings" on public.ratings for insert with check (auth.uid() = user_id);
create policy "admins manage ratings" on public.ratings for all using (public.is_admin()) with check (public.is_admin());

create policy "approved reviews are public" on public.reviews for select using (status = 'approved' or public.is_admin());
create policy "authenticated users create reviews" on public.reviews for insert with check (auth.uid() = user_id);
create policy "admins moderate reviews" on public.reviews for update using (public.is_admin()) with check (public.is_admin());

create policy "report owners and admins can read reports" on public.reports for select using (auth.uid() = user_id or public.is_admin());
create policy "authenticated users create reports" on public.reports for insert with check (auth.uid() = user_id);
create policy "admins moderate reports" on public.reports for update using (public.is_admin()) with check (public.is_admin());

create policy "users read own profile" on public.users for select using (auth.uid() = id or public.is_admin());
create policy "users update own profile" on public.users for update using (auth.uid() = id) with check (auth.uid() = id);
create policy "admins read admin table" on public.admins for select using (public.is_admin());

create policy "users manage own favorites" on public.favorites for all using (auth.uid() = user_id) with check (auth.uid() = user_id);
create policy "admins read moderation logs" on public.moderation_logs for select using (public.is_admin());
create policy "admins write moderation logs" on public.moderation_logs for insert with check (public.is_admin());
create policy "public indicators are readable" on public.regional_fuel_indicators for select using (true);
create policy "admins manage indicators" on public.regional_fuel_indicators for all using (public.is_admin()) with check (public.is_admin());
