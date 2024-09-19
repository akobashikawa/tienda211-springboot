FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/tienda211-0.0.1-SNAPSHOT.war tienda211.war
ENTRYPOINT ["java","-jar","/tienda211.war"]
