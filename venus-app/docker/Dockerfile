FROM openjdk:8u265-jre-buster
ADD ./venus-app-0.0.1-SNAPSHOT.jar /home/app/
WORKDIR /home/app/
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","venus-app-0.0.1-SNAPSHOT.jar"]
CMD jar -jar venus-app-0.0.1-SNAPSHOT.jar