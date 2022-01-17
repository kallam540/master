package com.nokia.gmfadapter.flink;


import com.nokia.gmfadapter.utils.SimpleAvroSchemaFlink;
import example.avro.User;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class GMFAdapter {
    public static void main(String[] args) throws Exception {
        // create execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");


        FlinkKafkaConsumer<User> flinkkafkaconsumer = new FlinkKafkaConsumer<User>("inputGMFCSV2", new SimpleAvroSchemaFlink(), properties);

        flinkkafkaconsumer.setStartFromEarliest();
        DataStream<User> stream = env.addSource(flinkkafkaconsumer);


        System.out.println(stream.print());
        stream.print();

        FlinkKafkaProducer<User> flinkKafkaProducer = new FlinkKafkaProducer<>("outputGMFCSV", new SimpleAvroSchemaFlink(), properties);
        stream.map(new MapFunction<User, User>() {
            @Override
            public User map(User userBehavior) throws Exception {
                userBehavior.setName(userBehavior.getName()+ " Name Updated");
                userBehavior.setFavoriteNumber(userBehavior.getFavoriteNumber()+ " Number Updated");
                userBehavior.setFavoriteColor(" Color Updated");
                return userBehavior;
            }
        }).addSink(flinkKafkaProducer);
        stream.print();
        env.execute("ReadFromKafka");
    }
}
