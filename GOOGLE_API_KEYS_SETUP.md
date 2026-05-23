# 🔑 **CONFIGURAÇÃO DAS API KEYS DO GOOGLE**

## 📋 **APIs Necessárias**

Para que o app funcione corretamente com postos REAIS do Google Maps, você precisa configurar estas APIs:

### 1. 🗺️ **Maps SDK for Android**
- **Finalidade**: Exibir o mapa interativo
- **Atual**: `AIzaSyAOHC1xNi6ZNdF447urnJ3OL3DeSlPdgA0` (sua chave)

### 2. 📍 **Places API** 
- **Finalidade**: Buscar postos de gasolina próximos
- **Necessário**: Chave específica para Places API

### 3. 🌍 **Geocoding API**
- **Finalidade**: Converter endereços em coordenadas
- **Necessário**: Pode usar a mesma chave das Places

## ⚙️ **Configuração no Google Cloud Console**

### Passo 1: Habilitar APIs
1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Vá em **APIs & Services > Library**
3. Busque e HABILITE estas APIs:
   - ✅ **Maps SDK for Android**
   - ✅ **Places API** 
   - ✅ **Geocoding API**

### Passo 2: Criar/Verificar API Keys
1. Vá em **APIs & Services > Credentials**
2. Clique em **+ CREATE CREDENTIALS > API Key**
3. Configure as restrições:
   - **Application restrictions**: Android apps
   - **Package name**: `br.com.vigiadeposto`
   - **SHA-1 certificate fingerprint**: `1E:EB:34:49:EB:19:E6:7A:40:1A:18:67:5F:22:A0:DB:4D:81:55:BA`

### Passo 3: Configurar APIs Permitidas
Na mesma tela da API Key, em **API restrictions**:
- ✅ Maps SDK for Android
- ✅ Places API
- ✅ Geocoding API

## 🔧 **Atualizar no Código**

### Arquivo 1: `local.properties`
```properties
MAPS_API_KEY=SUA_API_KEY_AQUI
PLACES_API_KEY=SUA_API_KEY_AQUI  # Pode ser a mesma
```

### Arquivo 2: `GooglePlacesService.kt` (linha 39)
```kotlin
private val placesApiKey = "SUA_API_KEY_AQUI"
```

### Arquivo 3: `google-services.json`
- Baixe a versão atualizada do Firebase Console
- Substitua o arquivo existente

## 💰 **Custos Aproximados**

| API | Limite Gratuito | Preço por 1.000 calls |
|-----|-----------------|----------------------|
| **Maps SDK** | Ilimitado | Grátis |
| **Places Nearby** | 0 grátis | $17.00 |
| **Geocoding** | $200 crédito/mês | $5.00 |

⚠️ **IMPORTANTE**: As Places API são pagas! Configure billing no Google Cloud.

## 🧪 **Teste da Configuração**

Após configurar, teste:

1. **Compile o app**: `gradlew assembleDebug`
2. **Instale no dispositivo**
3. **Conceda permissão de localização**
4. **Verifique os logs**: Procure por `"GooglePlaces"` no Logcat
5. **Resultado esperado**: 
   ```
   GooglePlaces: Buscando postos REAIS próximos a -23.xxx, -46.xxx
   GooglePlaces: Encontrados X postos reais
   ```

## 🚨 **Se der erro**

1. **Verifique se as APIs estão habilitadas**
2. **Confirme se a API Key tem as permissões corretas**
3. **Verifique se o SHA1 está correto**
4. **Confira se há créditos de billing**

## 📞 **Me informe quando estiver pronto!**

Envie suas API Keys e eu atualizo o código para usar as chaves reais!
