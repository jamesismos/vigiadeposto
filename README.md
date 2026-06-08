# Vigia de Posto

**by JAMESYSTEM**

App comunitario gratuito para consultar precos, seguranca e qualidade de postos de combustivel no Brasil.

## O Que Faz

- Mostra postos proximos no mapa com precos e reputacao
- Permite enviar precos e avaliacoes (anonimo ou identificado)
- Indica seguranca feminina, familia, banheiro, iluminacao, acessibilidade
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
