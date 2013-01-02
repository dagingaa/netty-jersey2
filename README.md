The code in this project is a proof-of-concept for integrating Netty and Jersey 2.0.
Dependency management is done using Maven.

The tricky part is implementing NettyHandlerContainer and NettyHandlerContainerProvider (which at the moment isn't used.
I recommend checking them out, and contributing as there are some missing features for full integration.

##Requirements##
- Maven
- Java version 1.6 (not tested with other versions as of now)

##Instructions##
- Clone the repository
- Run `mvn clean install` from the projects root directory
- `mvn package`
- `java -jar target/server-0.1-SNAPSHOT-jar-with-dependencies.jar
- Go to http://localhost:8080 to view

#TODO#
- SecurityContext is a black-box as of now. This needs a lot of work to get right.
- No idea what PropertiesDelegate is for. I think we need it for _something_.
- A lot of nice JavaDoc explaining what's going on.