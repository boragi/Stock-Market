FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/stock-market.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java","-jar","app.jar"]
