module "eventBroker" {
  source = "../../modules/eventBroker"

  rds_hostname = "host.docker.internal"
}
