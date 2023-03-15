FROM openjdk:8

ADD jar/user-webapp.jar user-webapp.jar

EXPOSE 8080 8443

ENTRYPOINT exec java $JAVA_OPTS -jar user-webapp.jar






