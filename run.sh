#!/bin/bash

echo "Running run.sh..."
echo "Current working directory: $(pwd)"
echo "Contents:"
ls -la

# Nạp biến môi trường từ .env nếu tồn tại
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
  echo "Loaded env variables from .env"
else
  echo ".env file not found!"
fi

PORT=${PORT:-8080}

echo "Starting app on port $PORT..."

exec java -jar backend-service.jar --server.port=$PORT
