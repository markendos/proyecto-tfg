FROM maven:3.6.3-openjdk-11-slim AS MAVEN_ENV
WORKDIR /build/
COPY pom.xml /build
COPY src /build/src
RUN mvn -Pprod clean package -DskipTests=true
ARG JAR_FILE=/build/target/proyecto-tfg-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
