docker-compose build
docker-compose up

curl -X POST 'http://localhost:9090/realms/master/protocol/openid-connect/token' -H 'Content-Type: application/x-www-form-urlencoded' -d 'client_id=bank-app' -d 'username=admin' -d 'password=admin' -d 'grant_type=password' -d 'scope=email profile' -d 'client_secret=NYexH97lVM4qTs0a5FAxN2CT2vIeZzvR'