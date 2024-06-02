FROM openjdk:17

ADD jar/user-webapp.jar user-webapp.jar

EXPOSE 8080 8443

ENTRYPOINT exec java $JAVA_OPTS -jar user-webapp.jar






