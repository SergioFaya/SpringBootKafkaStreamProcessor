# SpringBootKafkaStreamProcessor
Boilerplate code for kafka topic stream processor. To test it docker build the processor and run docker-compose up, try to produce some message on topic1 and should come in uppercase in topic2.

This spring boot project is intended to be a base for future processors giving a simple processor that allows us to check if the infraestructure is working correctly. 

The infraestructure is lift up by using a docker-compose file in wich we set up all the configuration of our kafka brokers using [wurstmeister/kafka](https://github.com/wurstmeister/kafka-docker). This initial configuration brings the opportunity to connect outside clients to the docker machines and also inside docker containers into a different listener.
With that configuration we can tests the processor addressing localhost:9092 and when the code is ready to be containerized just run `mvn package` and put it in the docker compose to test that everything is working,
