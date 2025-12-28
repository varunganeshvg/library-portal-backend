package com.example.bookservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff_assignment")
public class StaffAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// auth service id

    private Long staffId;      
    private String course;     
    private String section;    
    private Integer semester;  
    private String subjectName; 

    public StaffAssignment() {}

    public StaffAssignment(Long id, Long staffId, String course, String section,
                           Integer semester, String subjectName) {
        this.id = id;
        this.setStaffId(staffId);
        this.setCourse(course);
        this.setSection(section);
        this.setSemester(semester);
        this.setSubjectName(subjectName);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
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

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

   
}