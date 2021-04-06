FROM maven:3-jdk-11-slim AS MAVEN_ENV
WORKDIR /build/
COPY pom.xml /build
COPY src /build/src
RUN mvn -Pprod clean package -DskipTests=true

FROM arm64v8/openjdk:11-jre-slim
COPY  --from=MAVEN_ENV /build/target/proyecto-tfg-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
