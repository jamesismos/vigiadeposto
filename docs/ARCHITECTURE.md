# Arquitetura Vigia de Posto

## Produto

Vigia de Posto é um WebApp/PWA com três superfícies principais:

- experiência pública: busca, mapa, ranking, perfil do posto, comparação de preços e reputação;
- contribuição autenticada: preço, denúncia, elogio, foto opcional e publicação anônima;
- operação admin: moderação, analytics, heatmap, abuso, relatórios e auditoria.

## Stack

- Next.js 15 com App Router, React, TypeScript e TailwindCSS.
- Framer Motion para microinterações.
- Mapbox para mapa, clusterização e heatmap quando `NEXT_PUBLIC_MAPBOX_TOKEN` estiver configurado.
- Supabase Auth, PostgreSQL, Storage e Row Level Security.
- Vercel para deploy.
- Cloudflare Email Routing para emails transacionais encaminhados.

## Fluxo Anônimo

1. Usuário logado escolhe "Publicar como anônimo".
2. O registro mantém `user_id` para auditoria, anti-spam e LGPD.
3. A UI pública exibe apenas "Usuário anônimo".
4. RLS impede leitura pública de denúncias pendentes e dados do autor.
5. Admins veem contexto suficiente para abuso, sem expor identidade no perfil do posto.

## Índice de Preço Justo

Entrada:

- preço informado;
- média estadual;
- média ANP/Petrobras;
- ICMS do estado;
- reputação do posto;
- taxa recente de denúncia.

Saída:

- preço justo;
- acima da média;
- abaixo da média;
- suspeito;
- possível combustível adulterado quando há recorrência de relatos.

## Segurança

- RLS em todas as tabelas.
- Headers de segurança no Next.js.
- Validação de preço no banco.
- CAPTCHA invisível antes de denúncias e criação de posto.
- Rate limiting por IP, usuário, posto e combustível.
- Logs de moderação em `moderation_logs`.
- Storage com política privada para evidências sensíveis.
- Moderação automática antes de conteúdo público.
- Política LGPD: consentimento, exportação futura e exclusão/anomização.

## Escalabilidade

- Índices por posto, combustível e data em `fuel_prices`.
- Busca textual com GIN em postos.
- Separação entre eventos brutos e indicadores regionais agregados.
- Jobs futuros para ingestão ANP/Petrobras/ICMS.
- Cache por rota pública e invalidação por região.
- API pública futura com rate limit e chaves pagas.
