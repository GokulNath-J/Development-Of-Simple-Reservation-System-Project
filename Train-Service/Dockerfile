FROM openjdk:21-jdk-slim
WORKDIR Train/Dir/train
COPY target/Train-Service-0.0.1-SNAPSHOT.jar /Train/Dir/train/Reservation-Train-Service.jar
CMD ["java","-jar","Reservation-Train-Service.jar"]