FROM openjdk:latest

EXPOSE 9443

ADD target/lectures-service-0.0.1-SNAPSHOT.jar lectures-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","lectures-service-0.0.1-SNAPSHOT.jar"]