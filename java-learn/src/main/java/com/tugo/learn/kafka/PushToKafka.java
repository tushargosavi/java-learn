package com.tugo.learn.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class PushToKafka
{
  static Properties getProducerProperties()
  {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    return props;
  }

  public static void main(String[] args)
  {
    Properties props = getProducerProperties();
    KafkaProducer<byte[], byte[]> kp = new KafkaProducer<>(props);

    ProducerRecord<byte[], byte[]> pr = new ProducerRecord<>("test1", new byte[20], new byte[30]);
    kp.send(pr);
    kp.flush();
    kp.close();
  }

}
