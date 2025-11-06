package com.nextstep.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Customer {
	private String custId;
	private String custName;
	private String custAdharNo;
	private String custLoc;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate CustBdate;


		public LocalDate getCustBdate() {
			return CustBdate;
		}

		public void setCustBdate(LocalDate custBdate) {
			CustBdate = custBdate;
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

	@Override
	public String toString() {
		return "Customer [custId=" + custId + ", custName=" + custName + ", custAdharNo=" + custAdharNo + ", custLoc="
				+ custLoc + ", CustBdate=" + CustBdate + "]";
	}


}
