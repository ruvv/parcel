FROM bitnami/java:19

COPY ./target/gateway-0.0.1-SNAPSHOT.jar /opt/gateway.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/opt/gateway.jar"]