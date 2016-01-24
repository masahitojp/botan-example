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
$ docker run -i -t -d -e SLACK_TEAM=team -e SLACK_API_TOKEN=token masahitojp/botan-example:latest 
~~~


# Environment variables

|Environment variable|description|
|--------------|---------|
|SLACK_API_TOKEN   |Account's token. get one on https://api.slack.com/web#basics|

# license

Apache V2