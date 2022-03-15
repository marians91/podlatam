FROM 004253270836.dkr.ecr.eu-central-1.amazonaws.com/enel-common-containers/default-automation/adoptopenjdk/openjdk11-openj9:jdk-11.0.11_9_openj9-0.26.0-alpine
RUN apk add bash curl

COPY run.sh /run.sh
RUN chmod ug+x /run.sh
EXPOSE 8080
ADD virtualentity-1.0.jar virtualentity-1.0.jar
RUN /bin/sh -c 'java -Xshareclasses -Xscmx250M -Xscmaxaot220M -jar virtualentity-1.0.jar &' ; sleep 30 ; xargs kill -1 || exit 0
ENTRYPOINT ["sh", "-c", "./run.sh && java -XX:+ExitOnOutOfMemoryError -Xtune:virtualized -Xshareclasses -Xgcpolicy:optavgpause -Dspring.profiles.active=read-write,read-only -jar virtualentity-1.0.jar"]

