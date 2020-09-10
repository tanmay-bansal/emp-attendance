package  com.vibrent.empattendance.empattendance.messaging.consumer;

import com.vibrent.empattendance.empattendance.models.FooDto;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "Empattendance",
    topics = { "MYTOPIC1", "MYTOPIC2" },
    containerFactory = "kafkaListenerContainerFactory")
public class EmpattendanceListener {
  @KafkaHandler
  public void listenFoo(FooDto foo) {
    System.out.println("Received: " + foo);
  }
}
