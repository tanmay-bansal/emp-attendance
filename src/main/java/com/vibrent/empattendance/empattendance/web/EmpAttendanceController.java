package  com.vibrent.empattendance.empattendance.web;

import com.vibrent.empattendance.empattendance.models.FooDto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpAttendanceController {

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }
  
}
