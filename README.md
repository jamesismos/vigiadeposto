# Vigia de Posto

**by JAMESYSTEM**

WebApp/PWA open source e comunitário para transparência, fiscalização cidadã e monitoramento colaborativo de postos de combustível no Brasil.

> **Status:** Público, open source, sem custo por visualização de mapa, 100% baseado em OpenStreetMap.

## Filosofia

- **Privacidade primeiro.** Sem rastreamento de local sem consentimento explícito.
- **Sem anúncios invasivos.** Monetização só quando fizer sentido para o ecossistema.
- **Dados abertos.** Contribuições alimentam a comunidade, não corporações.
- **Segurança jurídica.** Nenhum texto livre é publicado sem moderação. Avaliações estruturadas primeiro.
- **Acessível.** PWA leve, offline parcial, gratuito para todos.

## O que resolve

1. Usuário não sabe se o preço do combustível está justo.
2. Mulheres e famílias buscam postos seguros para parar em estradas.
3. Cidadão quer denunciar adulteração sem expor sua identidade.
4. Faltam dados confiáveis, em tempo real e de fonte comunitária.

## Experiência Principal

- Home com busca, estatísticas nacionais e CTA de instalação PWA.
- Mapa colaborativo com filtros por gasolina, etanol, diesel, GNV e elétrico.
- Perfil de posto com reputação, preço, comparação estadual e indicadores de segurança.
- Relato estruturado: denúncia, elogio, informação e atualização de preço.
- Publicação anônima mesmo para usuário logado.
- Indicadores de segurança feminina, família, banheiro, acessibilidade e parada segura.
- Dashboard admin para moderação, denúncias, heatmap, abuso e relatórios.

## Stack Gratuita e Open Source

| Camada | Tecnologia | Custo |
|--------|-----------|-------|
| Frontend | Next.js 15, React, TypeScript, TailwindCSS | Gratuito |
| PWA | `manifest.json`, service worker, splash, offline parcial | Gratuito |
| Mapa | OpenStreetMap + MapLibre GL JS | Gratuito |
| Geocodificação | Nominatim | Gratuito |
| Backend | Supabase Free (PostgreSQL, Auth, RLS) | Gratuito |
| Deploy | Vercel Free | Gratuito |
| Analytics | Plausible Analytics self-host ou Umami | Gratuito |
| Monitoramento | Sentry Free | Gratuito |

### O que foi removido

- **Mapbox** → substituído por MapLibre GL JS + OpenStreetMap.
- **Google Maps** → eliminado. Sem dependência comercial de mapas.

## Estrutura do Projeto

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
  ARCHITECTURE.md
  WIREFRAMES.md
  MIGRACAO_OPENSTREETMAP.md
```

## Como Rodar

```bash
npm install
npm run dev
```

Acesse `http://localhost:3000`.

## Variáveis de Ambiente

Copie `.env.example` para `.env.local`:

```bash
NEXT_PUBLIC_SUPABASE_URL=
NEXT_PUBLIC_SUPABASE_ANON_KEY=
NEXT_PUBLIC_SITE_URL=https://seu-projeto.vercel.app
```

> Não há mais token de mapa comercial. MapLibre usa tiles livres do OpenStreetMap.

## Deploy Vercel

1. Importe este repositório na Vercel.
2. Configure o framework como Next.js.
3. Adicione as variáveis de ambiente.
4. Rode o deploy.
5. Aponte o domínio para o deployment ativo.

## Banco Supabase

O schema inicial está em:

```bash
supabase/migrations/001_initial_schema.sql
```

Inclui: `users`, `fuel_stations`, `fuel_prices`, `reviews`, `reports`, `moderation_logs`, `ratings`, `favorites`, `admins`, `regional_fuel_indicators`.

Rode a migration no Supabase SQL Editor ou via Supabase CLI.

## Segurança e LGPD

- Row Level Security ativo em todas as tabelas.
- Políticas separadas para leitura pública, criação autenticada e moderação admin.
- Headers contra clickjacking, sniffing e abuso de permissões.
- Dados anônimos ocultam identidade publicamente, mantendo trilha de auditoria.
- Preparado para CAPTCHA invisível, rate limiting, anti-spam e moderação automática.
- **Nenhum texto livre aparece sem moderação.** Avaliações estruturadas (estrelas, sim/não) são publicadas imediatamente. Comentários entram em fila de moderação.

## Documentação

- [Arquitetura](docs/ARCHITECTURE.md)
- [Wireframes](docs/WIREFRAMES.md)
- [Migração OpenStreetMap](docs/MIGRACAO_OPENSTREETMAP.md)

## Licença

MIT - Vigia de Posto by JAMESYSTEM.

---

**by JAMESYSTEM**
