package  com.vibrent.empattendance.empattendance.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.ObjectUtils;

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
