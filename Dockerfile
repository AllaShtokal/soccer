FROM openjdk:12.0.2-jdk
ADD target/soccer-0.0.1-SNAPSHOT.jar .
ADD dockerScript/wait .
RUN chmod +x /wait
CMD /wait && java -jar soccer-0.0.1-SNAPSHOT.jar --spring.profiles.active=container
EXPOSE 8080
