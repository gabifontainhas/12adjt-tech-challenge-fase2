FROM alpine/java:21-jdk

COPY target/techchallenge-0.0.1-SNAPSHOT.jar tech-challenge_2.jar

ENTRYPOINT ["java","-jar","/tech-challenge_2.jar"]



