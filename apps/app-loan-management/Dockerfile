FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG CONFIG_FILE=config/bootstrap.yml
COPY ${JAR_FILE} app.jar
COPY ${CONFIG_FILE} bootstrap.yml
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar --spring.config.location=bootstrap.yml"]