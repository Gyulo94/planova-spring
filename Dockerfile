FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon -x test

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/app.jar .

ENTRYPOINT ["java", "-jar", "app.jar"]
