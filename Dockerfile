FROM eclipse-temurin:21-jdk
ARG JAR_FILE=target/AutoShop-0.0.1-SNAPSHOT.jar
RUN mkdir /petproject
WORKDIR /petproject
COPY ${JAR_FILE} /petproject
ENTRYPOINT ["java", "-jar", "AutoShop-0.0.1-SNAPSHOT.jar"]