docker run --detach --name jms-tutorial-activemq -p 61616:61616 -p 8161:8161 --rm -v "./broker:/var/lib/artemis-instance/etc-override" apache/activemq-artemis:latest-alpine
