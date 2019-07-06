package com.tugo.learn.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

public class ConsumerBuilder
{
  private String topic;
  private String brokers;
  private String group;

  public ConsumerBuilder withTopic(String topic) {
    this.topic = topic;
    return this;
  }

  public ConsumerBuilder withBrokers(String hosts)
  {
    this.brokers = hosts;
    return this;
  }

  public ConsumerBuilder withGroupId(String groupId)
  {
    this.group = group;
    return this;
  }

  KafkaConsumer<byte[], byte[]> build() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      ByteArrayDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      ByteArrayDeserializer.class.getName());
    if (group != null) {
      props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
    }
    KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Collections.singleton(topic));
    return consumer;
  }
}
