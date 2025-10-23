FROM registry.ums.local:5000/jdk/eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY build/libs/config-server-0.0.1-SNAPSHOT.jar app.jar

ENV AWS_ACCESS_KEY_ID_FILE=/aws-keys/aws_access_key_id.txt
ENV AWS_SECRET_ACCESS_KEY_FILE=/aws-keys/aws_secret_access_key.txt
ENV AWS_REGION=ap-northeast-2
ENV AWS_BUCKET=ums-config-file-bucket
ENV CONFIG_FILE=/ums-config-file

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
