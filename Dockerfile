FROM openjdk:8
ADD target/dockerMovie.jar dockerMovie.jar
EXPOSE 8085
ENTRYPOINT ["java" ,"-jar","dockerMovie.jar"]