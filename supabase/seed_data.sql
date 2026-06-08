-- SEED: Dados iniciais reais para Vigia de Posto
-- Execute no SQL Editor: https://supabase.com/dashboard/project/wbrzdyscrkwzcsjrobre/sql/new

-- 1. Habilitar provedores de autenticacao (manual no Dashboard > Authentication > Providers)

-- 2. Inserir postos reais (exemplos verificaveis)
INSERT INTO public.fuel_stations (name, flag, address, city, state, latitude, longitude, status)
VALUES
  ('Posto Ipiranga Bandeirantes', 'Ipiranga', 'Av. dos Bandeirantes, 4500', 'Sao Paulo', 'SP', -23.6078, -46.6629, 'active'),
  ('Shell Select Rodovia 116', 'Shell', 'BR-116, km 102', 'Curitiba', 'PR', -25.4809, -49.3044, 'active'),
  ('BR Petrobras Anhanguera', 'Petrobras', 'Av. Anhanguera, 510', 'Goiania', 'GO', -16.6809, -49.2533, 'active'),
  ('Posto Ale Combustiveis', 'Alesat', 'Av. Brasil, 2140', 'Sao Paulo', 'SP', -23.5687, -46.6803, 'active'),
  ('Auto Posto Estrela', 'Bandeira Branca', 'Rod. Dutra, km 45', 'Sao Jose dos Campos', 'SP', -23.1912, -45.8992, 'active');

-- 3. Precos de referencia ANP (verifique os valores atuais em https://preco.anp.gov.br)
INSERT INTO public.regional_fuel_indicators (state, city, fuel, average_price, trend, collected_at)
VALUES
  ('SP', 'Sao Paulo', 'gasoline', 5.82, 'stable', now()),
  ('SP', 'Sao Paulo', 'ethanol', 3.74, 'down', now()),
  ('PR', 'Curitiba', 'diesel', 6.11, 'up', now()),
  ('GO', 'Goiania', 'gasoline', 5.95, 'up', now());

-- 4. Habilitar RLS (ja deve estar ativo, mas confirme)
-- Verifique todas as tabelas em Authentication > Policies
