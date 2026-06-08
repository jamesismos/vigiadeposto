# Configurar Supabase — Vigia de Posto

## 1. Rodar Migration (30 segundos)

1. Acesse: https://supabase.com/dashboard/project/wbrzdyscrkwzcsjrobre/sql/new
2. Cole TODO o conteudo de `supabase/migrations/001_initial_schema.sql`
3. Clique **Run**

## 2. Buckets Storage (ja criados via API)

- `evidence` — privado, imagens de denuncia (10MB max)
- `avatars` — publico, fotos de perfil (2MB max)
- `reports` — privado, PDFs/evidencias (50MB max)

## 3. Variaveis Vercel

No dashboard da Vercel (Settings > Environment Variables), adicione:

| Nome | Valor |
|------|-------|
| `NEXT_PUBLIC_SUPABASE_URL` | `https://wbrzdyscrkwzcsjrobre.supabase.co` |
| `NEXT_PUBLIC_SUPABASE_ANON_KEY` | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndicnpkeXNjcmt3emNzanJvYnJlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODA4NTY0MTIsImV4cCI6MjA5NjQzMjQxMn0.a0ldMR0l8tVu5eJMpAcM2m9Nixke-LU3xBjx3eUc5ZQ` |
| `NEXT_PUBLIC_SITE_URL` | `https://seu-dominio.vercel.app` |

**Apenas essas 3.** Nao precisa de token de mapa nem AdSense.

## 4. Deploy

```bash
npm run build
git push
```

Vercel detecta automaticamente e faz deploy.
