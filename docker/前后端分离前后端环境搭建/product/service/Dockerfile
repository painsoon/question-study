FROM openjdk:8-jre-alpine
MAINTAINER PSN

VOLUME /tmp

ADD demo-1.0-SNAPSHOT.jar app.jar

ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "-jar","-Duser.timezone=GMT+08","-Djava.security.egd=file:/dev/./urandom", "app.jar"]


