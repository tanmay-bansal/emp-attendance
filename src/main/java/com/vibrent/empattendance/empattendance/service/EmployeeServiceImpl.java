package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Employee;
import com.vibrent.empattendance.empattendance.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee getEmployeeDetails(String empId) {
        return employeeRepository.findByEmpId(empId);
    }

    @Override
    public void addEmployee(Employee emp) {
        employeeRepository.save(emp);
    }
}
