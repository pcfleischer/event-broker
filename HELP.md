# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.github.pcfleischer.event-broker' is invalid and this project uses 'com.github.pcfleischer.eventbroker' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/gradle-plugin/reference/html/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

kafka-topics.sh --list --bootstrap-server localhost:9092
kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic '.*'
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic eventSuppliereventProcessoreventCallbackConsumer-out-0 --from-beginning


[source,bash]
----
cd docker-compose
docker-compose up -d
docker-compose logs -f kafka-connect-cp|grep "Kafka Connect started"
----
