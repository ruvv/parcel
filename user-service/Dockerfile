FROM bitnami/java:19

COPY ./target/user-service-0.0.1-SNAPSHOT.jar /opt/user-service.jar

EXPOSE 9100

ENTRYPOINT ["java","-jar","/opt/user-service.jar"]