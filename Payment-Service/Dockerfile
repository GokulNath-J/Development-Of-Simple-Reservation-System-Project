FROM openjdk:21-jdk-slim
WORKDIR Payment/Dir/payment
COPY target/Payment-Service-0.0.1-SNAPSHOT.jar /Payment/Dir/payment/Reservation-Payment-Service.jar
CMD ["java","-jar","Reservation-Payment-Service.jar"]