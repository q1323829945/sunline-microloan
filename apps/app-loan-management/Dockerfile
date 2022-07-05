FROM registry.odc.sunline.cn/odc-basic/openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
ARG CONFIG_FILE=config/bootstrap_docker.yml
COPY ${JAR_FILE} app-loan-management.jar
COPY ${CONFIG_FILE} bootstrap.yml
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app-loan-management.jar --spring.config.location=bootstrap.yml"]