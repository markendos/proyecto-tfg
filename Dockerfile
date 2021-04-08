FROM maven:3.6.3-openjdk-11-slim AS MAVEN_ENV
WORKDIR /build/
COPY pom.xml /build
COPY src /build/src
RUN mvn -Pprod clean package -DskipTests=true
ADD /target/proyecto-tfg-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
