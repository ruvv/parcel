FROM bitnami/java:19

COPY ./target/discovery-0.0.1-SNAPSHOT.jar /opt/discovery.jar

EXPOSE 9001

ENTRYPOINT ["java","-jar","/opt/discovery.jar"]