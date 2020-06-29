FROM tomcat:9.0-jdk11

COPY ./dla-service/target/dla-service-0.1.0-SNAPSHOT.war /usr/local/tomcat/webapps/dla-service.war

EXPOSE 8080
