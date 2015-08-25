FROM java:8
ADD build/libs/botan-example-0.0.0.1.jar /usr/local/bin/botan-example.jar
ENTRYPOINT ["java", "-jar", "/usr/local/bin/botan-example.jar"]
CMD [""]
