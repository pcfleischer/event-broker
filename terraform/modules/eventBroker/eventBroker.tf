resource "kafka-connect_connector" "postgres-events-connector" {
  name = "postgres-events-connector"

  config = {
    "name"                                = "postgres-events-connector"
    "connector.class"                     = "io.debezium.connector.postgresql.PostgresConnector"
    "tasks.max"                           = "1"
    "plugin.name"                         = "wal2json"
    "database.hostname"                   = "host.docker.internal"
    "database.port"                       = "5432"
    "database.user"                       = "postgres"
    "database.password"                   = "postgres"
    "database.dbname"                     = "events"
    "database.server.name"                = "events"
    "schema.whitelist"                    = "public"
    "table.whitelist"                     = "public.event_entity"
    "key.converter"                       = "io.confluent.connect.avro.AvroConverter"
    "value.converter"                     = "io.confluent.connect.avro.AvroConverter"
    "key.converter.schema.registry.url"   = "http://schema-registry:8081"
    "value.converter.schema.registry.url" = "http://schema-registry:8081"
  }
}
