FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /suite
COPY qa/api/pom.xml .
RUN mvn -B dependency:go-offline
COPY qa/api/src src
CMD ["mvn", "-B", "test"]
