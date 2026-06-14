# 🔑 Como Obter o SHA-1 (Obrigatório)

## Método 1: Via Android Studio (Recomendado)

1. **Abra o Android Studio**
2. **Vá em:**
   - View > Tool Windows > Gradle
   - Ou clique no ícone Gradle na lateral direita

3. **Navegue até:**
   ```
   VigiadePosto > app > Tasks > android > signingReport
   ```

4. **Clique duas vezes em `signingReport`**
   - Isso vai executar o comando
   - O resultado aparecerá no console

5. **Procure por algo como:**
   ```
   Variant: debug
   Config: debug
   Store: D:\James\Programação\VigiadePosto\android\debug.keystore
   Alias: androiddebugkey
   MD5: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
   SHA1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
   SHA-256: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
   ```

6. **Copie o SHA1** (sem os dois pontos)

## Método 2: Via Terminal (se keytool estiver disponível)

```bash
keytool -list -v -keystore android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

## 🔧 Configurando no Firebase

1. **Acesse [Firebase Console](https://console.firebase.google.com/)**
2. **Selecione seu projeto: `vigia-de-posto`**
3. **Vá em:**
   - Configurações do projeto (ícone de engrenagem)
   - Geral
   - Seção "Seus aplicativos"
   - Clique no app Android

4. **Adicione a impressão digital SHA-1:**
   - Clique em "Adicionar impressão digital"
   - Cole o SHA-1 obtido
   - Clique em "Salvar"

## 🌐 Configurando Google Cloud Console

1. **Acesse [Google Cloud Console](https://console.cloud.google.com/)**
2. **Selecione o projeto: `vigia-de-posto`**
3. **Vá em:**
   - APIs e serviços > Credenciais
   - Encontre sua chave de API
   - Clique na chave para editar

4. **Configure restrições:**
   - **Restrição de aplicativo:** Aplicativos Android
   - **Adicione o SHA-1** obtido
   - **Package name:** `br.com.vigiadeposto`

5. **Salve as configurações**

## ✅ Verificação

Depois de configurar:
- Firebase deve reconhecer o app
- Google Maps deve funcionar
- Places API deve funcionar
- Geocoding API deve funcionar
