################ Build & Dev ################
# Build stage will be used:
# - for building the application for production
# - as target for development (see devspace.yaml)
FROM maven:3.9.9-eclipse-temurin-24-alpine as build

# Create project directory (workdir)
WORKDIR /app

# Install maven dependency packages
COPY pom.xml .
COPY ./twitter-data-model/pom.xml ./twitter-data-model/pom.xml
COPY ./tweet-service/pom.xml ./tweet-service/pom.xml
COPY ./dm-service/pom.xml ./dm-service/pom.xml
COPY ./user-service/pom.xml ./user-service/pom.xml
RUN mvn -T 1C install

# Add source code files to WORKDIR
ADD . .

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

RUN mvn package -T 1C -U -Dmaven.test.skip=true

################ Production ################
FROM eclipse-temurin:24-jre-alpine as production

WORKDIR /app

# Copy application binary from build/dev stage to the container
COPY --from=build /app/dm-service/target/*.jar /app/dm.jar
COPY --from=build /app/tweet-service/target/*.jar /app/tweet.jar
COPY --from=build /app/user-service/target/*.jar /app/user.jar

EXPOSE 8080

CMD ["/app/dm.jar"]