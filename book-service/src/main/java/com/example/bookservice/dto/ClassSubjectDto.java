package com.example.bookservice.dto;

public class ClassSubjectDto {

    private String subjectName;
    private Long staffUserId;
    private String staffName;

    public ClassSubjectDto(String subjectName, Long staffUserId, String staffName) {
        this.subjectName = subjectName;
        this.staffUserId = staffUserId;
        this.staffName = staffName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Long getStaffUserId() {
        return staffUserId;
    }

    public String getStaffName() {
        return staffName;
    }
}