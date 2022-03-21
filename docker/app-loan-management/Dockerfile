FROM openjdk:17.0.2-oracle 
LABEL maintainer lizheng2@sunline.com
ENV TZ=Asia/Shanghai
ADD app-loan-management-*.jar app-loan-management.jar
ADD application.yaml /config/application.yaml
ENTRYPOINT ["java","-jar","app-loan-management.jar","--spring.config.location=/config/application.yaml"]
