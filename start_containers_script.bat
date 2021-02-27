
docker container rm -f app
docker container rm -f mongo_db
docker network remove net

docker network create net
docker run -p 27017:27017 -d -v ./database:/data/db --name mongo_db --network=net mongo:4.4.4 mongod --replSet rs0

ping -n 6 127.0.0.1 > nul

docker container exec -t -i mongo_db bash -c "mongo --eval 'rs.initiate()'"


mvn clean package -DskipTests assembly:single spring-boot::repackage  && docker build . -t app_image && docker run -p 8080:8080 --name app --network=net app_image