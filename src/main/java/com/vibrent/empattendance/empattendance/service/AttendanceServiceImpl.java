package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import com.vibrent.empattendance.empattendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> getAttendance(String empId) {

        return attendanceRepository.findAllByEmpId(empId);
    }

    @Override
    public void addInTime(String empId) {
        Attendance attendance=new Attendance();
        attendance.setEmpId(empId);
    attendance.setAttId(attendance.getEmpId()+new SimpleDateFormat("yyyyMMdd").format(new Date()));
    attendance.setInTime(new Date());
    attendanceRepository.save(attendance);
    }

    @Override
    public void addOutTime(String empId) {
    Attendance attendance = attendanceRepository.findByAttId(""+empId+new SimpleDateFormat("yyyyMMdd").format(new Date()));
    System.out.println(""+empId+new SimpleDateFormat("yyyyMMdd").format(new Date()));
    attendance.setOutTime(new Date());
    attendanceRepository.save(attendance);
    }
}
