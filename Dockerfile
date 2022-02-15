#BUILD STAGE
FROM gradle:jdk17 AS GRADLE_BUILD_IMAGE

# DEFINING A FOLDER TO RECEIVE SOURCE CODE
ENV APP_HOME=/alteia/app/src
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME

# COPYING SOURCE CODE TO CREATED FOLDER
COPY . $APP_HOME

# BUILD APPLICATION
RUN gradle wrapper --gradle-version 7.3.3 --distribution-type bin
RUN gradle assemble -x test
RUN rm $APP_HOME/infrastructure/build/libs/*-plain.jar

#RUN STAGE
FROM openjdk:17.0.1
#Inserir nome do jar via pipeline
COPY --from=GRADLE_BUILD_IMAGE /alteia/app/src/infrastructure/build/libs/*.jar /alteia/app/cleanarch-springboot.jar
WORKDIR /alteia/app
EXPOSE 5000
ENTRYPOINT ["java","-jar","/alteia/app/cleanarch-springboot.jar"]