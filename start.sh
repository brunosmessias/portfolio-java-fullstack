#!/bin/bash

echo "🚀 Iniciando Portfolio Java Senior..."

echo "📦 Fazendo build da aplicação..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build concluído com sucesso!"
    echo "🐳 Iniciando containers Docker..."
    docker compose up --build
else
    echo "❌ Erro no build da aplicação!"
    exit 1
fi