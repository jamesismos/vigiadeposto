# 🚀 Checklist Final - Vigia de Posto

## ✅ JÁ CONFIGURADO
- [x] Dependências (AdMob, Firebase, Maps, Hilt)
- [x] Google-services.json
- [x] MAPS_API_KEY funcionando
- [x] ADMOB_APP_ID configurado
- [x] Firestore Rules prontas
- [x] SHA1 obtido: `1E:EB:34:49:EB:19:E6:7A:40:1A:18:67:5F:22:A0:DB:4D:81:55:BA`

## ⚠️ AÇÕES NECESSÁRIAS

### 1. Firebase Console
1. Ir para: https://console.firebase.google.com/project/vigia-de-posto
2. **Project Settings** → **General** → **Your apps**
3. Clicar em **br.com.vigiadeposto**
4. **Add fingerprint**: `1E:EB:34:49:EB:19:E6:7A:40:1A:18:67:5F:22:A0:DB:4D:81:55:BA`
5. **Baixar novo google-services.json** e substituir

### 2. Firestore Rules
1. Firebase Console → **Firestore Database** → **Rules**
2. Copiar conteúdo de `firestore_rules.rules` e publicar

### 3. Google Cloud Console
1. Ir para: https://console.cloud.google.com/
2. Selecionar projeto **vigia-de-posto**
3. **APIs & Services** → **Library**
4. Verificar se estão ativadas:
   - [x] Maps Android API
   - [ ] Google Sign-In API
   - [ ] Firebase Authentication API
   - [ ] Firestore API

### 4. AdMob (Se tiver site)
Criar arquivo `ads.txt` na raiz do seu domínio:
```
google.com, pub-4848539261829137, DIRECT, f08c47fec0942fa0
```

## 🧪 TESTE FINAL
Após as configurações acima:
```bash
gradlew clean assembleDebug
```

## 🚨 SEM SENHAS NECESSÁRIAS
- Firebase: Autentica via Google Services JSON
- AdMob: Usa App ID configurado
- Maps: Usa API Key configurada
- Firestore: Rules de segurança já definidas

## 📱 O QUE DEVE FUNCIONAR
- [x] Compilação sem erros
- [ ] Login com Google (após SHA1)
- [x] Anúncios AdMob
- [x] Mapas Google
- [ ] Salvamento no Firestore (após rules)
