FROM openjdk:11-jdk

COPY ./src ./reg_src
WORKDIR /reg_src

RUN javac registerComponent.java

CMD ["java", "registerComponent"]