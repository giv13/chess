FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY src ./
RUN echo "Main-Class: ru.giv13.chess.Main" > manifest.txt
RUN javac ru/giv13/chess/*.java ru/giv13/chess/piece/*.java
RUN jar cfm app.jar manifest.txt ru/giv13/chess/*.class ru/giv13/chess/piece/*.class

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]