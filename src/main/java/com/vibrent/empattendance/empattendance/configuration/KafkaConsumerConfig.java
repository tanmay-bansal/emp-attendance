package  com.vibrent.empattendance.empattendance.configuration;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.defaultConcurrency}")
  private int defaultConcurrency;
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  /**
   * Generates Kafka Logging error handler.
   * @return
   */
  @Bean
  public ConsumerFactory<String, byte[]> standardKafkaConsumerFactory() {
    Map<String, Object> consumerConfigs = new HashMap<>();
    consumerConfigs.put(GROUP_ID_CONFIG, "tp-service");
    consumerConfigs.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    consumerConfigs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfigs
        .put(KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
    consumerConfigs
        .put(VALUE_DESERIALIZER_CLASS_CONFIG,
            LazyDeserializer.class.getCanonicalName());
    Map<String, Object> configProps = this.getConfigProps(consumerConfigs);

    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  /**
   * Generates Kafka Container Factory.
   * @return
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, byte[]>
         kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
        new ConcurrentKafkaListenerContainerFactory<>();

    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
    factory.setAckDiscarded(true);

    // Add any custom Filtering if needed        

    factory.setConsumerFactory(standardKafkaConsumerFactory());
    factory.setConcurrency(defaultConcurrency);
    factory.setErrorHandler(loggingErrorHandler());
    return factory;
  }

  /**
   * Generates Kafka Logging error handler.
   * @return
   */
  @Bean
  public AbstractVibrentKafkaErrorHandler loggingErrorHandler() {
    return new ScrubbingVibrentKafkaErrorHandler();
  }

  /**
   * Generates Kafka Config Props.
   * @return
   */
  private Map<String, Object> getConfigProps(Map<String, Object> consumerConfigs) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.putAll(consumerConfigs);
    return props;
  }
}
