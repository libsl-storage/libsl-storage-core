FROM gradle:8.1.1-jdk17 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:22-jdk
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/libsl-storage-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar", "--spring.profiles.active=prod"]