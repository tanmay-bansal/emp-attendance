package  com.vibrent.empattendance.empattendance.configuration;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.util.ObjectUtils;

/**
* The listener takes message payload as bytes array.
* It then it can be parsed into different types of dto.
*/
@Slf4j
public abstract class AbstractVibrentKafkaErrorHandler implements ErrorHandler {

  @Override
  public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
    ConsumerRecord<?, ?> output = removePiiData(data);
    log.error("Error while processing: " + ObjectUtils.nullSafeToString(output), thrownException);
  }

  @Override
  public void handle(Exception thrownException,
                     ConsumerRecord<?, ?> data,
                     Consumer<?, ?> consumer) {
    ConsumerRecord<?, ?> output = removePiiData(data);
    log.error("Error while processing: " + ObjectUtils.nullSafeToString(output), thrownException);
  }

  @Override
  public void handle(Exception thrownException,
                     List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer,
                     MessageListenerContainer container) {
    log.error(thrownException.getMessage(), thrownException);
  }

  @Override
  public void clearThreadState() {
    // NOSONAR
  }


  public abstract ConsumerRecord<?, ?> removePiiData(ConsumerRecord<?, ?> data);

}
