FROM openjdk:8-jdk-alpine
COPY ./target/admicroservice-0.0.1.jar ./
CMD ["java","-jar","admicroservice-0.0.1.jar"]
