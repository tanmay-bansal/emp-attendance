package  com.vibrent.empattendance.empattendance.configuration;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

/**
 * This deserializer is used in the listener to prevent costly deserialization during kafka message
 * filtering process.
 */
public class LazyDeserializer implements Deserializer {

  @Override
  public void configure(Map configs, boolean isKey) {
    // We love Sonar
  }

  @Override
  public Object deserialize(String topic, byte[] data) {
    return data;
  }

  @Override
  public void close() {
    // We love Sonar
  }
}
