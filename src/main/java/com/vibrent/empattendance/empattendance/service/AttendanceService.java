package com.vibrent.empattendance.empattendance.service;

import com.vibrent.empattendance.empattendance.dto.Attendance;

public interface AttendanceService {

    public Attendance getAttendance(String empId);

    public void addInTime(String empId);

    public void addOutTime(String empId);
}
