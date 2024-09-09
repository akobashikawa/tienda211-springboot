FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/tienda102-0.0.1-SNAPSHOT.war tienda102.war
ENTRYPOINT ["java","-jar","/tienda102.war"]
