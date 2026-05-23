#!/bin/bash

echo "🔑 Obtendo SHA-1 do debug keystore..."
echo ""

# Verificar se o keystore existe
if [ ! -f "app/debug.keystore" ]; then
    echo "❌ Erro: app/debug.keystore não encontrado!"
    echo "Execute primeiro: keytool -genkey -v -keystore app/debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname \"CN=Android Debug,O=Android,C=US\""
    exit 1
fi

# Tentar obter SHA-1
echo "📋 Executando keytool..."
keytool -list -v -keystore app/debug.keystore -alias androiddebugkey -storepass android -keypass android 2>/dev/null

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SHA-1 obtido com sucesso!"
    echo ""
    echo "🔧 Próximos passos:"
    echo "1. Copie o SHA-1 acima"
    echo "2. Vá para Firebase Console > Configurações do projeto"
    echo "3. Adicione a impressão digital SHA-1"
    echo "4. Configure Google Cloud Console com a mesma chave"
else
    echo ""
    echo "❌ Erro ao obter SHA-1"
    echo ""
    echo "💡 Alternativa: Use o Android Studio"
    echo "1. Abra o projeto no Android Studio"
    echo "2. View > Tool Windows > Gradle"
    echo "3. VigiadePosto > app > Tasks > android > signingReport"
    echo "4. Clique duas vezes em signingReport"
fi
