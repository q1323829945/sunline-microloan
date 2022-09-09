FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG CONFIG_FILE=config/bootstrap_docker.yml
COPY ${JAR_FILE} bdo-app.jar
COPY ${CONFIG_FILE} bootstrap.yml
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /bdo-app.jar --spring.config.location=bootstrap.yml"]