FROM maven:3.8.3-openjdk-17 as mvn_build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2  mvn clean package -Dmaven.test.skip

RUN mkdir -p target/extracted
RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
#LABEL PROJECT_NAME=web-shop

ARG EXTRACTED=/app/target/extracted
COPY --from=mvn_build ${EXTRACTED}/dependencies/ ./
COPY --from=mvn_build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=mvn_build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=mvn_build ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]



#WORKDIR application
#ADD mvn/${project.build.finalName}.jar ${project.build.finalName}.jar
#RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract
#
#FROM eclipse-temurin:17-jdk-alpine
#LABEL PROJECT_NAME=${project.artifactId} PROJECT=${project.id}
#
#EXPOSE 8080
#
#WORKDIR application
#COPY --from=builder application/dependencies/ ./
#COPY --from=builder application/spring-boot-loader/ ./
#COPY --from=builder application/snapshot-dependencies/ ./
#COPY --from=builder application/application/ ./
#ENTRYPOINT ["java","Djava.security.egd=file:/dev/./urandom","org.springframework.boot.loader.JarLauncher"]
