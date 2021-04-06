FROM openjdk:11
VOLUME /tmp
COPY ./target/proyecto-tfg-0.0.1-SNAPSHOT.jar proyecto-tfg-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","proyecto-tfg-0.0.1-SNAPSHOT.jar"]
