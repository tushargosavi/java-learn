package com.tugo.learn.kafka;

import java.util.Properties;

public class PushToKafka
{
  Properties getProducerProperties()
  {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    return props;
  }

}
