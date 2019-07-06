package com.tugo.learn.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerExmaple
{
  static class MyRebalanceListener implements ConsumerRebalanceListener
  {
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions)
    {
      for(TopicPartition tp : partitions) {
        System.out.println("Revoke partition " + tp);
      }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions)
    {
      for(TopicPartition tp : partitions) {
        System.out.println("Start receiving data from " + tp);
      }
    }
  }

  public static void main(String[] args)
  {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("enable.auto.commit", "false");
    props.put("auto.commit.interval.ms", "1000");
    props.put("session.timeout.ms", "30000");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    consumer.subscribe(Collections.singleton("test"), new MyRebalanceListener());

    int count = 0;
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
      for (ConsumerRecord<String, String> record : records) {
        count++;
        long keyLen = record.key() != null? record.key().length(): 0;
        long valLen = record.value() != null? record.value().length(): 0;
        System.out.println("Received record " + count + " key len = " + keyLen + " val len = " + valLen + " value " + record.value());
      }
    }
  }
}
