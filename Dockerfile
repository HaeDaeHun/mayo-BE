FROM openjdk:17-slim # x86_64 (AMD64)용 이미지

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/app.jar"]