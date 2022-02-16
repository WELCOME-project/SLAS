FROM tomcat:9.0-jdk11

COPY ./dla-service/target/dla-service-1.1.0-SNAPSHOT.war /usr/local/tomcat/webapps/dla-service.war
COPY ./welcome-demos/target/welcome-demos-1.1.0-SNAPSHOT.war /usr/local/tomcat/webapps/welcome-demos.war

EXPOSE 8080
