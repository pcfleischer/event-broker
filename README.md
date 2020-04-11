# Spring Event Broker

This is a "seed" project to demonstrate a combination of technologies to implement common event streaming use cases.

* Spring Boot MVC
  * Event Api using Reactor
* Spring Cloud Stream
  * Processing Application
* Data Streaming Pipeline (Change Data Capture)
  * Kafka
  * Kafka Connect
  * Debezium Kafka Connect
  * Debezium Postgres

# Getting Started
Start by composing the docker container of the dependency services at the root directory.

```
docker-compose up -d
```

**What's included?**
* Confluent Platform
  * Kafka
  * Zookeeper
  * Schema Registry
  * Connect (Confluent Connectors)
* Debezium
  * Connect (Debezium Connectors)
* PostgreSQL
  * Wal2Json Logical Replication Plugin

# Using Event Broker
## Reactive REST Api

The reactive REST Api is designed to demonstrate an asynchronous processor:

```
# build stuff
./gradlew build
# init stuff
./gradlew initPrimary
./gradlew initReplica
# run stuff
./gradlew :api:bootRun
./gradlew :processors:bootRun
```

https://github.com/spring-cloud/spring-cloud-stream-samples

This sends an event to kafka and listens for a callback on a spring consumer function binding.
```
curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:9080/events -d '{
  "name": "event",
  "properties": {
      "thing": "happened"
      }
 }'
 ```
**Response:**
The processor function runs a toUpper and outputs an object to call back binding.
 ```
 {
  "NAME": "EVENT",
  "PROPERTIES": {
      "THING": "HAPPENED"
      }
 }
 ```

**Monitoring Kafka Topics:**

If you're using control center you can watch the topics in the UI to verify that the mesages are being sent.

`http://localhost:9021/clusters/<cluster_id>/management/topics/pcfleischer.eventbroker.event_source/message-viewer`

```
docker exec -ti event-broker_kafka_1 kafka-topics --list --bootstrap-server localhost:9092

docker exec -ti event-broker_kafka_1 kafka-console-consumer --bootstrap-server localhost:9092 --topic pcfleischer.eventbroker.event_source --from-beginning

docker exec -ti event-broker_kafka_1 kafka-console-consumer --bootstrap-server localhost:9092 --topic pcfleischer.eventbroker.event_callback --from-beginning
```

**Clearing Kafka Topics:**

NOTE: clearing out all topics will affect kafka connect and kafka control center.
```
docker exec -ti event-broker_kafka_1 kafka-topics --delete --bootstrap-server localhost:9092 --topic '.*'
```


## Routed Datasource

https://fable.sh/blog/splitting-read-and-write-operations-in-spring-boot/


## Change Data Capture

### Postgres Wal2Json

Debezium docker image includes wal2json which is required cdc data pipeline will fail if another postgres image is running.

The article below details how to setup aws postgres rds with kinesis.  We will be using the logical replication feature to replicate this.

https://aws.amazon.com/blogs/database/stream-changes-from-amazon-rds-for-postgresql-using-amazon-kinesis-data-streams-and-aws-lambda/
https://debezium.io/documentation/reference/1.1/connectors/postgresql.html

### Debezium Connector

The debezium postgres connector creates a replication slot and streams changes into a kafka topic. In order to create the connector instance you can 

By default the Debezium connectors us JSON serialization, which is easier to consume "across platforms".  Though many 

https://debezium.io/documentation/reference/configuration/avro.html

```
"key.converter": "org.apache.kafka.connect.json.JsonConverter",
"value.converter": "org.apache.kafka.connect.json.JsonConverter"
```

it is possible to set the debezium connector for the entire connect node with environment variables:

```
KEY_CONVERTER=io.confluent.connect.avro.AvroConverter
VALUE_CONVERTER=io.confluent.connect.avro.AvroConverter
CONNECT_KEY_CONVERTER_SCHEMA_REGISTRY_URL=http://schema-registry:8081
CONNECT_VALUE_CONVERTER_SCHEMA_REGISTRY_URL=http://schema-registry:8081
```


#### Creating PostgreSQL Connector

##### Create Connector with Terraform

Requires terraform "see brew install terraform"

```
# install the kafka connect plugin via tarball
./scripts/install_tf_k_connect.sh

# use the environment variable directory, init and apply the resources
cd terraform/environments/<environment>/
terraform init
terraform plan
terraform apply

# or just run the event broker module
terraform apply -target=module.eventBroker
```

##### Create Connector with CURL

```
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors -d '{
  "name": "postgres-events-connector",
  "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "plugin.name": "wal2json",
      "database.hostname": "host.docker.internal",
      "database.port": "5432",
      "database.user": "genesisuser",
      "database.password": "a1111111",
      "database.dbname" : "events",
      "database.server.name": "events",
      "schema.whitelist": "public",
      "table.whitelist": "public.event_entity",
      "key.converter": "io.confluent.connect.avro.AvroConverter",
      "value.converter": "io.confluent.connect.avro.AvroConverter",
      "key.converter.schema.registry.url": "http://schema-registry:8081",
      "value.converter.schema.registry.url": "http://schema-registry:8081"
      }
 }'
```

#### Other options:
```
"slot.drop_on_stop": true,
"include.unknown.datatypes": false,
"topic.selection.strategy": "topic_per_schema"
```

#### Monitoring: Connector(s)
```
curl http://localhost:8083/connectors/postgres-events-connector
curl http://localhost:8083/connectors/postgres-events-connector/status
curl http://localhost:8083/connectors/postgres-events-connector/tasks
```

#### Monitoring Kafka Topics

```
docker exec -ti event-broker_kafka_1 kafka-console-consumer --bootstrap-server localhost:9092 --topic events.public.event_entity --from-beginning
```

Debezium Postgres Slots
```
-- view all slots
select * 
from pg_replication_slots

-- dropping the debezium slot
select pg_drop_replication_slot(slot_name) 
from pg_replication_slots 
where slot_name = 'debezium';
```

`curl -i -X DELETE http://localhost:8083/connectors/postgres-events-connector`

NOTE: wait for rebalance to complete and connector no longer is returned in the connector list

`curl http://localhost:8083/connectors`

**Sample Debezium Envelope**
```
{
    "after": {
        "id": "2",  
        "details_json": "{ \"test\": \"create\" }"
    },
    "source": {
        "version": "0.8.3.Final",
        "name": "events",
        "db": "events",
        "ts_usec": 1585328358642906000,
        "txId": 63566,
        "lsn": 381295845,
        "schema": "public",
        "table": "event_entity",
        "snapshot": false
    },
    "op": "c",
    "ts_ms": 1585328358650
}
```

### S3 Sink Connector

If you intend to save all the message off to long term storage an option might be to use the s3 connector from confluent.

https://docs.confluent.io/current/connect/kafka-connect-s3/index.html

#### AWS Access Setup

* Localstack
  * No credentials needed.
  * Override storage url  "s3.proxy.url": "http://localstack:4572"
* IAM Roles
  * using EC2 or ECS attach roles permissions to instances
* AWS Credentials
  * Project default - mount your aws profile ~/.aws credentials setup
  * Environment variables - explicitly add these to your container environment
  
```
AWS_ACCESS_KEY_ID: *******
AWS_SECRET_KEY: *******
```

**Create S3 Connector**

This example is work in progress, it uses local stack which is not currently part of the docker composition.

```
 curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:18083/connectors -d '{
  "name": "s3.events.public.event_entity",
  "config": {
      "connector.class": "io.confluent.connect.s3.S3SinkConnector",
      "tasks.max": "1",
      "topics": [
        "events.public.event_entity"
      ],
      "s3.region": "us-east-1",
      "s3.bucket.name": "s3-sink",
      "aws.accessKeyId": "access",
      "aws.secretKey": "secret",
      "data.format": "AVRO",
      "format.class": "io.confluent.connect.s3.format.parquet.ParquetFormat",
      "key.converter": "io.confluent.connect.avro.AvroConverter",
      "value.converter": "io.confluent.connect.avro.AvroConverter",
      "key.converter.schema.registry.url": "http://schema-registry:8081",
      "value.converter.schema.registry.url": "http://schema-registry:8081",
      "flush.size": 1,
      "storage.class":  "io.confluent.connect.s3.storage.S3Storage",
      "s3.proxy.url": "http://localstack:4572"
  }
 }'
```


### Kafka Connect Rest Api

* [Kafka Connect REST Api](https://docs.confluent.io/current/connect/references/restapi.html)

```
curl http://localhost:8083/connectors/postgres-events-connector
curl http://localhost:8083/connectors/postgres-events-connector/status
curl http://localhost:8083/connectors/postgres-events-connector/tasks
```

### Schema Registry

In this demo project we aren't using code generation of objects from avro or demonstrating schema changes but the schema registry is required for debezium connectors.

https://www.baeldung.com/spring-cloud-stream-kafka-avro-confluent

```
curl http://localhost:8081/subjects/
curl http://localhost:8081/subjects/
curl http://localhost:8081/subjects/events.public.event_entity-key/versions/1
```

```
{
    "subject": "events.public.event_entity-key",
    "version": 1,
    "id": 1,
    "schema": "{\"type\":\"record\",\"name\":\"Key\",\"namespace\":\"events.public.event_entity\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"}],\"connect.name\":\"events.public.event_entity.Key\"}"
}
```


```
{
    "type": "record",
    "name": "Key",
    "namespace": "events.public.event_entity",
    "fields": [
        {
            "name": "id",
            "type": "string"
        }
    ],
    "connect.name": "events.public.event_entity.Key"
}
```

```
{
    "subject": "events.public.event_entity-value",
    "version": 1,
    "id": 2,
    "schema": "{\"type\":\"record\",\"name\":\"Envelope\",\"namespace\":\"events.public.event_entity\",\"fields\":[{\"name\":\"before\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"client_id\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"details_json\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"error\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"ip_address\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"realm_id\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"session_id\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"event_time\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"type\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"user_id\",\"type\":[\"null\",\"string\"],\"default\":null}],\"connect.name\":\"events.public.event_entity.Value\"}],\"default\":null},{\"name\":\"after\",\"type\":[\"null\",\"Value\"],\"default\":null},{\"name\":\"source\",\"type\":{\"type\":\"record\",\"name\":\"Source\",\"namespace\":\"io.debezium.connector.postgresql\",\"fields\":[{\"name\":\"version\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"db\",\"type\":\"string\"},{\"name\":\"ts_usec\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"txId\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"lsn\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"schema\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"table\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"snapshot\",\"type\":[{\"type\":\"boolean\",\"connect.default\":false},\"null\"],\"default\":false},{\"name\":\"last_snapshot_record\",\"type\":[\"null\",\"boolean\"],\"default\":null}],\"connect.name\":\"io.debezium.connector.postgresql.Source\"}},{\"name\":\"op\",\"type\":\"string\"},{\"name\":\"ts_ms\",\"type\":[\"null\",\"long\"],\"default\":null}],\"connect.name\":\"events.public.event_entity.Envelope\"}"
}
```

```
{
    "type": "record",
    "name": "Envelope",
    "namespace": "events.public.event_entity",
    "fields": [
        {
            "name": "before",
            "type": [
                "null",
                {
                    "type": "record",
                    "name": "Value",
                    "fields": [
                        {
                            "name": "id",
                            "type": "string"
                        },
                        {
                            "name": "client_id",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "details_json",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "error",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "ip_address",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "realm_id",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "session_id",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "event_time",
                            "type": [
                                "null",
                                "long"
                            ],
                            "default": null
                        },
                        {
                            "name": "type",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        },
                        {
                            "name": "user_id",
                            "type": [
                                "null",
                                "string"
                            ],
                            "default": null
                        }
                    ],
                    "connect.name": "events.public.event_entity.Value"
                }
            ],
            "default": null
        },
        {
            "name": "after",
            "type": [
                "null",
                "Value"
            ],
            "default": null
        },
        {
            "name": "source",
            "type": {
                "type": "record",
                "name": "Source",
                "namespace": "io.debezium.connector.postgresql",
                "fields": [
                    {
                        "name": "version",
                        "type": [
                            "null",
                            "string"
                        ],
                        "default": null
                    },
                    {
                        "name": "name",
                        "type": "string"
                    },
                    {
                        "name": "db",
                        "type": "string"
                    },
                    {
                        "name": "ts_usec",
                        "type": [
                            "null",
                            "long"
                        ],
                        "default": null
                    },
                    {
                        "name": "txId",
                        "type": [
                            "null",
                            "long"
                        ],
                        "default": null
                    },
                    {
                        "name": "lsn",
                        "type": [
                            "null",
                            "long"
                        ],
                        "default": null
                    },
                    {
                        "name": "schema",
                        "type": [
                            "null",
                            "string"
                        ],
                        "default": null
                    },
                    {
                        "name": "table",
                        "type": [
                            "null",
                            "string"
                        ],
                        "default": null
                    },
                    {
                        "name": "snapshot",
                        "type": [
                            {
                                "type": "boolean",
                                "connect.default": false
                            },
                            "null"
                        ],
                        "default": false
                    },
                    {
                        "name": "last_snapshot_record",
                        "type": [
                            "null",
                            "boolean"
                        ],
                        "default": null
                    }
                ],
                "connect.name": "io.debezium.connector.postgresql.Source"
            }
        },
        {
            "name": "op",
            "type": "string"
        },
        {
            "name": "ts_ms",
            "type": [
                "null",
                "long"
            ],
            "default": null
        }
    ],
    "connect.name": "events.public.event_entity.Envelope"
}
```

### Reference Documentation
For further reference, please consider the following sections:
* [Kafka Connect REST Api](https://docs.confluent.io/current/connect/references/restapi.html)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/gradle-plugin/reference/html/)

