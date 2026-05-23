#!/bin/bash

echo "🚀 Testando Vigia de Posto App"
echo "================================"
echo ""

# Verificar se adb está disponível
if ! command -v adb &> /dev/null; then
    echo "❌ ADB não encontrado!"
    echo "💡 Certifique-se que o Android SDK está instalado"
    exit 1
fi

# Verificar dispositivos conectados
echo "📱 Verificando dispositivos..."
adb devices

echo ""
echo "🔍 Verificando logs do app..."
echo "Pressione Ctrl+C para parar"
echo ""

# Filtrar logs do app
adb logcat | grep -E "(TestUtils|VigiaDePosto|Firebase|Maps)"

echo ""
echo "✅ Teste concluído!"
echo ""
echo "📋 O que verificar:"
echo "1. Firebase connection test: SUCCESS"
echo "2. Google Maps connection test: SUCCESS"
echo "3. App funcionando sem crashes"
echo "4. Login com Google funcionando"
echo "5. Mapa carregando postos"
