package  com.vibrent.empattendance.empattendance.configuration;

import org.apache.kafka.clients.consumer.ConsumerRecord;


/**
 * This is the default Error handler that scrubs PII from log data. 
 * Each app might have its own implementation for scrubbing.
 * SO please add the necessary implementation logic
 */
public class ScrubbingVibrentKafkaErrorHandler extends AbstractVibrentKafkaErrorHandler {

  @Override
  public  ConsumerRecord<?, ?> removePiiData(ConsumerRecord<?, ?> data) {
    //TODO Implement Me
    return null;
  }

}
