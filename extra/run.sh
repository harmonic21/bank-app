cd ../
mvn clean install
cd extra
docker-compose build
docker-compose up -d bank_db
docker-compose up -d keycloack
echo "Пожалуйста, зайдите по адресу http://localhost:9090 и выполните импорт realm из файла realm-export.json в keycloak"
read -p "Продолжить?"
docker-compose up