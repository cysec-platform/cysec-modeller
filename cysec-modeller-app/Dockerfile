FROM adoptopenjdk/openjdk11:alpine-jre as extractor
WORKDIR application
COPY target/*.jar cysec-modeller.jar
RUN java -Djarmode=layertools -jar cysec-modeller.jar extract


FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR application

RUN addgroup -S cysec-modeller && adduser -S cysec-modeller -G cysec-modeller
RUN mkdir -p ./data
RUN chown cysec-modeller ./data

USER cysec-modeller:cysec-modeller

COPY --from=extractor application/spring-boot-loader/ ./
RUN true
COPY --from=extractor application/dependencies/ ./
RUN true
COPY --from=extractor application/snapshot-dependencies/ ./
RUN true
COPY --from=extractor application/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
