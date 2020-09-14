package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import com.vibrent.empattendance.empattendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public Attendance getAttendance(String empId) {
       return attendanceRepository.findByEmpId(empId);
    }

    @Override
    public void addInTime(String empId) {
        Attendance attendance=new Attendance();
        attendance.setEmpId(empId);
    attendance.setAttId(attendance.getEmpId()+new Date());
    attendance.setInTime(new Date());
    attendanceRepository.save(attendance);
    }

    @Override
    public void addOutTime(String empId) {
    Attendance attendance = attendanceRepository.findByEmpId(empId);
    attendance.setOutTime(new Date());
    attendanceRepository.save(attendance);
    }
}
