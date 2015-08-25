FROM java:8
ADD build/docker/botan-example.jar /usr/local/bin/botan-example.jar
ENTRYPOINT ["java", "-jar", "/usr/local/bin/botan-example.jar"]
CMD [""]
