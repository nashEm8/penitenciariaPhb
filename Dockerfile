# Usa o JDK 21 como base
FROM eclipse-temurin:21-jdk

# Instala dependências básicas
RUN apt update && apt install -y unzip wget && \
    wget https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.16.zip && \
    unzip glassfish-7.0.16.zip && \
    mv glassfish7 /opt/glassfish

# Copia o WAR para a pasta de deploy do GlassFish
COPY target/AppPenitenciaria /opt/glassfish/glassfish/domains/domain1/autodeploy/

# Expõe as portas do GlassFish
EXPOSE 8080 4848

# Inicia o GlassFish no modo verbose
CMD ["/opt/glassfish/bin/asadmin", "start-domain", "--verbose"]
