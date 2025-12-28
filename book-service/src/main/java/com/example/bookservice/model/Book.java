package com.example.bookservice.model;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String category;
    private String downloadLink;
    private Long uploadedByStaffId;// auth
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime uploadedAt;
    
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    
    private String course;
    private String section;
    private Integer semester;

    // 4) Default constructor - required by JPA
    public Book() {
    }

    // 5) Constructor with all fields
    public Book(Long id, String title, String author, String category, String downloadLink,Long uploadedByStaffId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.downloadLink = downloadLink;
        this.setUploadedByStaffId(uploadedByStaffId);
        
       
        
        
    }

    // 6) Getters and setters (so Spring/Jackson can read/write the fields)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDownloadLink() {
        return downloadLink;
    }   

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

	public Long getUploadedByStaffId() {
		return uploadedByStaffId;
	}

	public void setUploadedByStaffId(Long uploadedByStaffId) {
		this.uploadedByStaffId = uploadedByStaffId;
	}
	
	public LocalDateTime getUploadedAt() {
	    return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
	    this.uploadedAt = uploadedAt;
	}

	public LocalDateTime getLastUpdatedAt() {
	    return lastUpdatedAt;
	}

	public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
	    this.lastUpdatedAt = lastUpdatedAt;
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
	
}
