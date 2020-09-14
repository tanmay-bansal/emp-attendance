package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Employee;

public interface EmployeeService {

    public Employee getEmployeeDetails(String empId);

    public void addEmployee(Employee emp);

}
