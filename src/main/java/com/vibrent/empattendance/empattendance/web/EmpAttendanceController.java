package  com.vibrent.empattendance.empattendance.web;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import com.vibrent.empattendance.empattendance.service.AttendanceService;
import com.vibrent.empattendance.empattendance.service.EmployeeService;
import com.vibrent.empattendance.empattendance.dto.Employee;
import com.vibrent.empattendance.empattendance.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmpAttendanceController {

  @Autowired
  EmployeeService employeeService;

  @Autowired
  AttendanceService attendanceService;

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

  @GetMapping("/employee/{empId}")
  public Employee getEmployeeDetails(@PathVariable String empId) {
    return employeeService.getEmployeeDetails(empId);
  }

  @GetMapping("/attendance/{empId}")
  public List<Attendance> getAttendance(@PathVariable String empId) {
    return attendanceService.getAttendance(empId);
  }

    @PostMapping("/employee")
  public ResponseEntity<String> addEmployee(@RequestBody Employee emp) {
    employeeService.addEmployee(emp);
    return new ResponseEntity<>("Added", HttpStatus.CREATED);
  }

  @PostMapping("/attendance")
  public ResponseEntity<String> addInTime(@RequestBody Attendance attendance) {
    attendanceService.addInTime(attendance.getEmpId());
    return new ResponseEntity<>("Added", HttpStatus.CREATED);
  }

  @PutMapping("/attendance")
  public ResponseEntity<String> addOutTime(@RequestBody Attendance attendance) {
    if(attendance.getEmpId()!=null)
    attendanceService.addOutTime(attendance.getEmpId());
    return new ResponseEntity<>("Updated", HttpStatus.ACCEPTED);
  }

}
