# 🔐 Como Corrigir Google Sign-in - Vigia de Posto

## ⚠️ **PROBLEMA ATUAL:**
Google Sign-in não funciona porque falta configurar OAuth Client ID correto.

## 🛠️ **SOLUÇÃO PASSO A PASSO:**

### **1. 🔑 Adicionar SHA1 no Firebase Console**
1. Vá para: https://console.firebase.google.com/project/vigia-de-posto
2. **Project Settings** (⚙️) → **General** → **Your apps**
3. Clique no app **br.com.vigiadeposto**
4. Clique em **Add fingerprint**
5. Cole o SHA1: `1E:EB:34:49:EB:19:E6:7A:40:1A:18:67:5F:22:A0:DB:4D:81:55:BA`
6. Clique **Save**

### **2. 📱 Ativar Google Sign-in**
1. No Firebase Console → **Authentication**
2. **Sign-in method** → **Google**
3. **Enable** → **Project support email** → **Save**

### **3. 📥 Baixar novo google-services.json**
1. Após adicionar SHA1 → **Download google-services.json**
2. Substituir o arquivo atual em `android/google-services.json`

### **4. 🔍 Obter Web Client ID**
1. No novo `google-services.json`, procure por:
```json
"oauth_client": [
  {
    "client_id": "123456789012-abcdefg.apps.googleusercontent.com",
    "client_type": 3
  }
]
```
2. Copie o `client_id` que tem `client_type: 3`

### **5. 💾 Atualizar código**
Substitua no `LoginScreen.kt`:
```kotlin
// TROCAR ESTA LINHA:
.requestIdToken("123456789012-abcdefghijklmnopqrstuvwxyz123456.apps.googleusercontent.com")

// PELO SEU CLIENT_ID REAL:
.requestIdToken("SEU_WEB_CLIENT_ID_AQUI")
```

## 🚀 **RESUMO - O QUE FAZER:**

1. ✅ **Firebase Console**: Adicionar SHA1 
2. ✅ **Authentication**: Ativar Google Sign-in
3. ✅ **Download**: Novo google-services.json
4. ✅ **LoginScreen**: Usar Web Client ID real

## 🎯 **APÓS ESSAS CONFIGURAÇÕES:**
- Google Sign-in funcionará perfeitamente
- Usuários poderão fazer login com conta Google
- Firebase Authentication estará operacional

## 📱 **TESTE:**
1. Rebuild o app: `gradlew clean assembleDebug`
2. Instale o APK no dispositivo
3. Teste Google Sign-in na tela de login

---

**💡 IMPORTANTE**: O SHA1 é específico para cada keystore. O que obtivemos é do debug keystore. Para produção, você precisará do SHA1 do keystore de release.
