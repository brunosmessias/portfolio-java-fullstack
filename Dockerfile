FROM tomcat:10-jdk17-temurin

# Remove aplicações padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado pelo Maven para o Tomcat
COPY target/portfolio-java-fullstack.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080