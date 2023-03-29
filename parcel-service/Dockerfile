FROM bitnami/java:19

COPY ./target/parcel-service-0.0.1-SNAPSHOT.jar /opt/parcel-service.jar

EXPOSE 9200

ENTRYPOINT ["java","-jar","/opt/parcel-service.jar"]