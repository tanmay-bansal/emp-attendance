package com.vibrent.empattendance.empattendance.repository;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import com.vibrent.empattendance.empattendance.dto.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByEmpId(String empId);
    //Object save(Employee employee);
}
