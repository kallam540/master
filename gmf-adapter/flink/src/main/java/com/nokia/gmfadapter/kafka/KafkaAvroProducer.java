package com.nokia.gmfadapter.kafka;


import example.avro.EventType;
import example.avro.User;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaAvroProducer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal producer
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "10");
        // avro part
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        Producer<String, User> producer = new KafkaProducer<String, User>(properties);

        String topic = "inputGMFCSV2";

        // copied from avro examples
        User customer = User.newBuilder()
                .setFavoriteColor("Telupu -1 28/12/2021")
                .setName("Pilli Bithar -1 28/12/2021")
                .setFavoriteNumber("1 to 10 28/12/2021")
                .setEventType(EventType.meeting)
                .build();

        ProducerRecord<String, User> producerRecord = new ProducerRecord<String, User>(
                topic, customer
        );

        System.out.println(customer);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println(metadata);
                } else {
                    exception.printStackTrace();
                }
            }
        });

        producer.flush();
        producer.close();

    }
}

