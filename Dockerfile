FROM tomcat:9.0
ARG lang

COPY slas-${lang}/slas-${lang}-service/target/slas-${lang}-service-1.1.0-SNAPSHOT.war /usr/local/tomcat/webapps/slas-services-${lang}.war

EXPOSE 8080
