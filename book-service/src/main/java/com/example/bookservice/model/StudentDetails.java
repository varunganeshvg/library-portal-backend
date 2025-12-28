package com.example.bookservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="student_details")
public class StudentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long userId;
	
	private String name;
	
	private String email;
	
	private String course;
	
	private String section;
	
	private Integer semester;
	
	private String rollNo;
	
	public StudentDetails() {}
	
	public StudentDetails(Long id,Long userId,String name, String email,String course, String section, Integer semester, String rollNo )
	{
		this.id=id;
		this.setUserId(userId);
		this.setName(name);
		this.setEmail(email);
		this.setCourse(course);
		this.setSection(section);
		this.setSemester(semester);
		this.setrollNo(rollNo);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setrollNo(String rollNo) {
		this.rollNo = rollNo;
	}
 
	
}
