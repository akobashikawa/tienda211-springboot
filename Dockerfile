FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/tienda101-0.0.1-SNAPSHOT.war tienda101.war
ENTRYPOINT ["java","-jar","/tienda101.war"]
