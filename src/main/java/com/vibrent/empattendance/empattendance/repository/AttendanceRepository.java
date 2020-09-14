package com.vibrent.empattendance.empattendance.repository;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,String> {

    Attendance findByEmpId(String empId);
   // Object save(Attendance attendance);

}
