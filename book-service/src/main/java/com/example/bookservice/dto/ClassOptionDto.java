package com.example.bookservice.dto;

public class ClassOptionDto {

    private String course;
    private String section;
    private Integer semester;

    // ✅ Default constructor (required by Spring)
    public ClassOptionDto() {}

    // ✅ Constructor for easy mapping
    public ClassOptionDto(String course, String section, Integer semester) {
        this.course = course;
        this.section = section;
        this.semester = semester;
    }

    // ===== Getters & Setters =====

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
}