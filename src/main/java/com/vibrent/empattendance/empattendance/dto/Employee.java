package  com.vibrent.empattendance.empattendance.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee implements Serializable {
  private static final long serialVersionUID = -6949375489579665L;
  @Id
  private String empId;
  @Column(name = "emp_name")
  private String empName;
  @Column(name = "emp_address")
  private String empAddress;
  @Column(name = "mob_num")
  private String mobNum;
  @Column(name = "email")
  private String email;
  @Column(name = "designation")
  private String designation;

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public String getEmpName() {
    return empName;
  }

  public void setEmpName(String empName) {
    this.empName = empName;
  }

  public String getEmpAddress() {
    return empAddress;
  }

  public void setEmpAddress(String empAddress) {
    this.empAddress = empAddress;
  }

  public String getMobNum() {
    return mobNum;
  }

  public void setMobNum(String mobNum) {
    this.mobNum = mobNum;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  @Override
  public String toString() {
    return "Employee{" +
            "empId='" + empId + '\'' +
            ", empName='" + empName + '\'' +
            ", empAddress='" + empAddress + '\'' +
            ", mobNum='" + mobNum + '\'' +
            ", email='" + email + '\'' +
            ", designation='" + designation + '\'' +
            '}';
  }
}
