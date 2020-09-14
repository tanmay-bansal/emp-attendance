package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Attendance;

import java.util.List;

public interface AttendanceService {

    public List<Attendance> getAttendance(String empId);

    public void addInTime(String empId);

    public void addOutTime(String empId);
}
