FROM eclipse-temurin:21-jdk
ARG JAR_FILE=target/FinalProject_AutoShop-0.0.1-SNAPSHOT.jar
RUN mkdir /c36
WORKDIR /c36
COPY ${JAR_FILE} /c36
ENTRYPOINT ["java", "-jar", "FinalProject_AutoShop-0.0.1-SNAPSHOT.jar"]