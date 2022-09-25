package com.cst438.domain;

public class StudentDTO {
	public String name;
	public String email;
	public int student_id;
	public int statusCode = 0;
	public String status = null;
	
	@Override
	public String toString() {
		return "StudentDTO [student_email=" + this.email 
				+ ", name=" + this.name + ",student_id=" 
				+ this.student_id + ",statusCode=" + this.statusCode
				+ ",status=" + this.status + "]";
	}
}
