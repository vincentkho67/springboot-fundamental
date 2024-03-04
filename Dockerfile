FROM openjdk:17-alpine

ADD target/bit-pod-0.0.1-SNAPSHOT.jar bit-pod.jar

CMD ["java", "-jar", "bit-pod.jar"]
