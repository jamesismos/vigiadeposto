# Vigia de Posto

**by JAMESYSTEM**

App comunitario gratuito para avaliar postos de combustivel: seguranca, estrutura, banheiro, iluminacao, acessibilidade e preco.

## O Que Faz

- Avalia posto: seguro? Tem banheiro? Trocador? Iluminacao? Acessivel? Preco justo?
- Mostra postos no mapa com reputacao e indicadores de estrutura
- Permite enviar avaliacoes e precos (anonimo ou identificado)
- Modera textos antes de publicar (protecao juridica)

## Para Quem E

Motoristas, caminhoneiros, familias e mulheres que precisam decidir onde parar com seguranca.

## Como Rodar

```bash
npm install
npm run dev
```

## Como Validar

```bash
npm run lint
npm run build
```

## Publicacao

Deploy automatico na Vercel. Variaveis necessarias no `.env.local`:

```bash
NEXT_PUBLIC_SUPABASE_URL=
NEXT_PUBLIC_SUPABASE_ANON_KEY=
NEXT_PUBLIC_SITE_URL=
```

Mapa usa OpenStreetMap gratuito — sem custo por visualizacao.

---

**by JAMESYSTEM**
