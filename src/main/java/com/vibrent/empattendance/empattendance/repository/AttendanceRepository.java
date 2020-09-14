package com.vibrent.empattendance.empattendance.repository;

import com.vibrent.empattendance.empattendance.dto.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,String> {

    Attendance findByEmpId(String empId);
    List<Attendance> findAllByEmpId(String empId);
    Attendance findByAttId(String attId);
   // Object save(Attendance attendance);

}
