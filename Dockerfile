FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/tienda104-0.0.1-SNAPSHOT.war tienda104.war
ENTRYPOINT ["java","-jar","/tienda104.war"]
