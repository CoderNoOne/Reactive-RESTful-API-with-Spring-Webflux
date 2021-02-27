docker network remove net
docker network create net

docker run -p 27017:27017  -d --name mongo_db --network net mongo_image
docker run -p 8080:8080 --network net app_image