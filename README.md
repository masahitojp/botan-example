# botan-example

Examples of chat bot using the botan(https://github.com/masahitojp/botan)

# requirement

* Java8

## build jar

~~~sh
$ ./gradlew jar
~~~

### build jar and build docker image

~~~sh
$ ./gradlew buildDockerImage
~~~

~~~sh
$ docker run -i -t -d -e SLACK_TEAM=team -e SLACK_USERNAME=user -e SLACK_PASSWORD=password -e SLACK_ROOM=room masahitojp/botan-example:latest 
~~~


# Environment variables

|Environment variable|description|
|--------------|---------|
|SLACK_TEAM    |slack team name|
|SLACK_USERNAME|user name      |
|SLACK_PASSWORD|password       |
|SLACK_ROOM| room name (general etc)|

# license

Apache V2