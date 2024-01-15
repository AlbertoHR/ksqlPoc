FROM hseeberger/scala-sbt:11.0.14.1_1.6.2_2.12.15 as builder
WORKDIR /app
COPY . /app
RUN sbt clean assembly

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/scala-2.12/scalaClient-assembly-0.1.0-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "scalaClient-assembly-0.1.0-SNAPSHOT.jar"]
