# Vigia de Posto

WebApp/PWA moderno para transparência, fiscalização cidadã e monitoramento colaborativo de postos de combustível no Brasil.

O projeto preserva o app Android existente e adiciona uma versão web em Next.js 15 preparada para Vercel, Supabase, Mapbox e instalação como aplicativo.

## Experiência

- Home institucional com busca, estatísticas nacionais, preço médio do dia e CTA de instalação PWA.
- Mapa colaborativo com filtros por gasolina, etanol, diesel, GNV e elétrico.
- Perfil de posto com reputação, preço, comparação estadual, ANP/Petrobras e indicadores de segurança.
- Relato de experiência com denúncia, elogio, informação e atualização de preço.
- Publicação anônima mesmo para usuário logado.
- Indicadores de segurança feminina, família, banheiro, acessibilidade e parada segura.
- Dashboard admin para moderação, denúncias, heatmap, abuso e relatórios.
- Seção "Ajude o Projeto" com PIX e Bitcoin.

## Stack Web

- Next.js 15, React, TypeScript e TailwindCSS.
- Framer Motion, lucide-react e Mapbox GL.
- Supabase Auth, PostgreSQL, RLS e Storage.
- Vercel para deploy.
- PWA com `manifest.json`, service worker, splash e offline parcial.

## Como Rodar

```bash
npm install
npm run dev
```

Acesse `http://localhost:3000`.

## Variáveis de Ambiente

Copie `.env.example` para `.env.local` e preencha:

```bash
NEXT_PUBLIC_SUPABASE_URL=
NEXT_PUBLIC_SUPABASE_ANON_KEY=
NEXT_PUBLIC_MAPBOX_TOKEN=
NEXT_PUBLIC_ADSENSE_CLIENT=ca-pub-3090285265842642
NEXT_PUBLIC_SITE_URL=https://seu-projeto.vercel.app
```

## Deploy Vercel

1. Importe este repositório na Vercel.
2. Configure o framework como Next.js.
3. Adicione as variáveis de ambiente.
4. Rode o deploy.
5. Aponte o domínio para o deployment ativo.

O erro `DEPLOYMENT_NOT_FOUND` indica que o domínio está apontando para um deployment inexistente/removido ou para um projeto Vercel sem build ativo. Um novo deploy a partir desta raiz resolve a base do problema.

## Banco Supabase

O schema inicial está em:

```bash
supabase/migrations/001_initial_schema.sql
```

Inclui:

- `users`
- `fuel_stations`
- `fuel_prices`
- `reviews`
- `reports`
- `moderation_logs`
- `ratings`
- `favorites`
- `admins`
- `regional_fuel_indicators`

Rode a migration no Supabase SQL Editor ou via Supabase CLI.

## Segurança

- Row Level Security ativo em todas as tabelas.
- Políticas separadas para leitura pública, criação autenticada e moderação admin.
- Headers contra clickjacking, sniffing e abuso de permissões.
- Dados anônimos ocultam identidade publicamente, mantendo trilha de auditoria.
- Preparado para CAPTCHA invisível, rate limiting, anti-spam e moderação automática.

## Arquitetura e Wireframes

- [Arquitetura](docs/ARCHITECTURE.md)
- [Wireframes](docs/WIREFRAMES.md)

## Estrutura Web

```text
src/
  app/
    admin/
    offline/
    globals.css
    layout.tsx
    page.tsx
  components/
  lib/
public/
  manifest.json
  sw.js
supabase/
  migrations/
docs/
```

## Monetização Futura

- anúncios discretos;
- postos patrocinados;
- analytics premium;
- relatórios regionais;
- ranking premium;
- API paga.

AdSense preparado: `ca-pub-3090285265842642`.
