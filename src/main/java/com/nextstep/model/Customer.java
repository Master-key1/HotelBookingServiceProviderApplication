package com.nextstep.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customer_details")
public class Customer {
	@Id
	private String custId;
	private String custName;
	private String custAdharNo;
	private String custLoc;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate CustBdate;
	private String custPhoneNo;
	private byte age;

	@PrePersist
	public void generateId() {
		this.custId = UUID.randomUUID().toString(); // Auto generate String ID
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAdharNo() {
		return custAdharNo;
	}

	public void setCustAdharNo(String custAdharNo) {
		this.custAdharNo = custAdharNo;
	}

	public String getCustLoc() {
		return custLoc;
	}

	public void setCustLoc(String custLoc) {
		this.custLoc = custLoc;
	}

	public LocalDate getCustBdate() {
		return CustBdate;
	}

	public void setCustBdate(LocalDate custBdate) {
		CustBdate = custBdate;
	}

	public String getCustPhoneNo() {
		return custPhoneNo;
	}

	public void setCustPhoneNo(String custPhoneNo) {
		this.custPhoneNo = custPhoneNo;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Customer [custId=" + custId + ", custName=" + custName + ", custAdharNo=" + custAdharNo + ", custLoc="
				+ custLoc + ", CustBdate=" + CustBdate + ", custPhoneNo=" + custPhoneNo + ", age=" + age + "]";
	}

}
