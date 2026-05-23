# Instruções de Build - Vigia de Posto

## 🚀 Build Manual

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 17
- Android SDK 34

### Passos para Build

1. **Abra o projeto no Android Studio**
   ```bash
   # Abra o Android Studio e selecione "Open an existing project"
   # Navegue até a pasta VigiadePosto e selecione
   ```

2. **Sincronize o projeto**
   - Aguarde o Android Studio baixar as dependências
   - Verifique se não há erros de compilação

3. **Configure as chaves**
   - **Firebase**: Certifique-se que `google-services.json` está na pasta `app/`
   - **Google Maps**: Configure `MAPS_API_KEY` no arquivo `local.properties`

4. **Build Debug**
   ```bash
   # No Android Studio: Build > Build Bundle(s) / APK(s) > Build APK(s)
   # Ou via terminal (se gradlew estiver disponível):
   ./gradlew assembleDebug
   ```

5. **Instalar no dispositivo**
   ```bash
   # Conecte um dispositivo ou emulador
   # No Android Studio: Run > Run 'app'
   # Ou via terminal:
   ./gradlew installDebug
   ```

## 📱 Testando o App

### 1. Verificar Logs
```bash
adb logcat | grep "TestUtils"
```

### 2. Testes de Funcionalidade
- ✅ **Autenticação**: Teste login com Google
- ✅ **Mapa**: Verifique se o mapa carrega
- ✅ **Localização**: Teste permissões de localização
- ✅ **Firebase**: Verifique conexão com Firestore
- ✅ **Navegação**: Teste todas as telas

### 3. Verificar Configurações
- **Maps API Key**: Deve estar funcionando
- **Firebase**: Conexão estabelecida
- **Google Sign-In**: Configurado corretamente

## 🐛 Solução de Problemas

### Erro de Compilação
1. Execute `./gradlew clean`
2. Sincronize o projeto no Android Studio
3. Verifique se todas as dependências estão resolvidas

### Erro de Maps
1. Verifique se `MAPS_API_KEY` está configurada
2. Confirme se a API está ativada no Google Cloud Console
3. Verifique se o SHA-1 está configurado

### Erro de Firebase
1. Confirme se `google-services.json` está na pasta `app/`
2. Verifique se o package name está correto
3. Confirme se as dependências estão atualizadas

## 📦 APK de Teste

O APK debug será gerado em:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🔧 Configurações Finais

### ProGuard
- Configurado para Firebase e Google Services
- Otimizações habilitadas para release

### Signing
- Debug keystore configurado
- Release signing configurado

### Build Variants
- **Debug**: Para desenvolvimento e teste
- **Release**: Para produção

## 📋 Checklist de Teste

- [ ] App compila sem erros
- [ ] Firebase Auth funciona
- [ ] Google Maps carrega
- [ ] Localização funciona
- [ ] Navegação entre telas
- [ ] Bottom navigation
- [ ] Adicionar posto
- [ ] Votar em postos
- [ ] Ranking funciona
- [ ] Informações carregam

## 🎯 Próximos Passos

1. **Teste no dispositivo físico**
2. **Configure Firebase Console**
3. **Configure Google Cloud Console**
4. **Teste todas as funcionalidades**
5. **Gere APK release para produção**
