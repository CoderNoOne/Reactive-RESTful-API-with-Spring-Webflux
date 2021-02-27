docker container rm -f app && mvn clean package -DskipTests assembly:single spring-boot::repackage  &&  docker build . -t app_image && docker run -p 8080:8080 --name app --network=net app_image



