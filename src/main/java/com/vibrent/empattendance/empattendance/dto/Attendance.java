package com.vibrent.empattendance.empattendance.dto;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "attendance")
@Data
public class Attendance {
    @Id
    private String attId;
    @Column(name = "emp_id")
    private String empId;
    @Column(name = "in_time")
    private Date inTime;
    @Column(name = "out_time")
    private Date outTime;

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attId='" + attId + '\'' +
                ", empId='" + empId + '\'' +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                '}';
    }
}
