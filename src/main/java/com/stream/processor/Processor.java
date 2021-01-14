package com.stream.processor;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.StreamsMetrics;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
class Processor {


    @Autowired
    public void process() {
        // export KAFKA_BROKER=localhost:9092
        String broker = System.getenv("KAFKA_BROKER");
        if(broker == null){
            throw new IllegalArgumentException("no env variable for KAFKA_BROKER");
        }

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "myapp");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        // serialization defaults
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("topic1", Consumed.with(Serdes.String(), Serdes.String()));

        textLines
                .mapValues(value -> value.toUpperCase())
                .to("topic2", Produced.with(Serdes.String(), Serdes.String()));

        // for logging
        textLines.print(Printed.toSysOut());

        /**
         * KTable<String, Long> wordCounts = textLines
         *                 .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
         *                 .groupBy((key, word) -> word)
         *                 .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));
         * wordCounts.toStream().to("topic2", Produced.with(Serdes.String(), Serdes.Long()));
         */

        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        streams.start();
    }
}
