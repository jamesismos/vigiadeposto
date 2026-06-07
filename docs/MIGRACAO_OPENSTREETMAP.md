# MIGRACAO_OPENSTREETMAP.md — Vigia de Posto Web

**by JAMESYSTEM**

## Status da Migração

**Data:** 2026-06-07
**De:** Mapbox GL JS + Google Maps
**Para:** MapLibre GL JS + OpenStreetMap + Nominatim
**Projeto:** Vigia de Posto Web (Next.js 15 + React 19)

## Por que Migrar

| Problema | Solução com OSM |
|----------|-----------------|
| Custo por visualização de mapa | Gratuito, sem limites |
| Dependência comercial (Mapbox, Google) | 100% open source |
| Lock-in de dados | Dados abertos, comunidade |
| Privacidade do usuário | Sem rastreamento de uso de mapa |

## O que Mudou no Código

### 1. Dependências (`package.json`)

**Removido:**
- `mapbox-gl`
- `@types/mapbox-gl`

**Adicionado:**
- `maplibre-gl`
- `@types/maplibre-gl`

### 2. Variáveis de Ambiente

**Removido:**
```bash
# NÃO USAR MAIS
NEXT_PUBLIC_MAPBOX_TOKEN=...
```

**Mantido:**
```bash
NEXT_PUBLIC_SUPABASE_URL=
NEXT_PUBLIC_SUPABASE_ANON_KEY=
NEXT_PUBLIC_SITE_URL=
```

### 3. Componente de Mapa

**Antes (Mapbox):**
```typescript
import mapboxgl from 'mapbox-gl';
mapboxgl.accessToken = process.env.NEXT_PUBLIC_MAPBOX_TOKEN;
const map = new mapboxgl.Map({ ... });
```

**Depois (MapLibre):**
```typescript
import maplibregl from 'maplibre-gl';
const map = new maplibregl.Map({
  container: 'map',
  style: {
    version: 8,
    sources: {
      osm: {
        type: 'raster',
        tiles: ['https://tile.openstreetmap.org/{z}/{x}/{y}.png'],
        tileSize: 256,
        attribution: '&copy; OpenStreetMap contributors'
      }
    },
    layers: [{
      id: 'osm',
      type: 'raster',
      source: 'osm'
    }]
  },
  center: [-47.9292, -15.7801],
  zoom: 10
});
```

### 4. Tiles e Atribuição

| Provedor | URL | Atribuição |
|----------|-----|------------|
| OpenStreetMap padrão | `https://tile.openstreetmap.org/{z}/{x}/{y}.png` | © OpenStreetMap contributors |
| Stadia Maps (opcional) | `https://tiles.stadiamaps.com/tiles/alidade_smooth/{z}/{x}/{y}.png` | © Stadia Maps © OpenStreetMap |
| CartoDB (opcional) | `https://{a,b,c,d}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png` | © CartoDB © OpenStreetMap |

> **Regra:** Sempre manter atribuição visível. É obrigatório por licença ODbL.

### 5. Busca Geográfica

**Antes:** Google Places API
**Depois:** Nominatim OpenStreetMap

```typescript
// Busca de endereço
const response = await fetch(
  `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}`
);
const data = await response.json();
```

> **Nota:** Respeite a política de uso do Nominatim. Limite de 1 requisição/segundo. Para produção com alto volume, considere host próprio do Nominatim.

### 6. Overpass API (Busca de Postos)

Para encontrar postos de combustível no OpenStreetMap:

```typescript
const overpassQuery = `
  [out:json];
  node["amenity"="fuel"](around:5000,${lat},${lon});
  out;
`;

const response = await fetch('https://overpass-api.de/api/interpreter', {
  method: 'POST',
  body: overpassQuery
});
```

## Checklist de Migração

- [ ] Remover `mapbox-gl` e `@types/mapbox-gl` do `package.json`
- [ ] Adicionar `maplibre-gl` e `@types/maplibre-gl`
- [ ] Reescrever `MapPreview.tsx` para MapLibre
- [ ] Remover `NEXT_PUBLIC_MAPBOX_TOKEN` do `.env.example`
- [ ] Adicionar atribuição OSM no mapa
- [ ] Implementar busca com Nominatim
- [ ] Testar em mobile (touch, zoom, pan)
- [ ] Testar offline (cache de tiles)
- [ ] Verificar performance com 1000+ marcadores
- [ ] Atualizar `README.md`
- [ ] Atualizar `MODULES_CATALOG.md`

## Vantagens Alcançadas

| Métrica | Antes | Depois |
|---------|-------|--------|
| Custo de mapa | ~$200-500/mês (alto volume) | $0 |
| Dependência | Mapbox Inc. | Comunidade OSM |
| Dados | Proprietários | Abertos (ODbL) |
| Customização | Limitada pelo plano | Total |
| Offline | Complexo | Simplificado |

## Próximos Passos

1. Implementar cache de tiles no service worker para offline
2. Integrar Overpass API para importar postos reais do OSM
3. Avaliar PostGIS no Supabase para queries geoespaciais nativas
4. Considerar tiles próprios se o volume exceder limites do Nominatim/OSM

## Referências

- [MapLibre GL JS Docs](https://maplibre.org/maplibre-gl-js/docs/)
- [OpenStreetMap Tile Usage Policy](https://operations.osmfoundation.org/policies/tiles/)
- [Nominatim Usage Policy](https://operations.osmfoundation.org/policies/nominatim/)
- [Overpass API](https://wiki.openstreetmap.org/wiki/Overpass_API)

---

**by JAMESYSTEM** | Stack gratuita, aberta e escalável.
