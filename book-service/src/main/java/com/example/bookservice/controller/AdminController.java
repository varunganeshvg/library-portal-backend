package com.example.bookservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookservice.model.Book;
import com.example.bookservice.model.StaffAssignment;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.repository.StaffAssignmentRepository;
import com.example.bookservice.repository.StaffDetailsRepository;
import com.example.bookservice.model.StaffDetails;
import com.example.bookservice.model.StudentDetails;
import com.example.bookservice.service.StudentService;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final StaffAssignmentRepository staffAssignmentRepository;
	
	private final BookRepository bookRepository;
	
	private final StudentService studentService;
	
	private final StaffDetailsRepository staffDetailsRepository;
	
	
	AdminController(
	        StaffAssignmentRepository staffAssignmentRepository,
	        BookRepository bookRepository,
	        StaffDetailsRepository staffDetailsRepository,
	        StudentService studentService
	) {
	    this.staffAssignmentRepository = staffAssignmentRepository;
	    this.bookRepository = bookRepository;
	    this.staffDetailsRepository = staffDetailsRepository;
	    this.studentService=studentService;
	}
	
	@PostMapping("/assign")
	public StaffAssignment createAssignment(@RequestBody StaffAssignment assignment) {

	    // assignment.getStaffId() = staff_details.id (from frontend)

	    StaffDetails staff = staffDetailsRepository.findById(assignment.getStaffId())
	            .orElseThrow(() -> new RuntimeException("Staff not found"));

	    // Convert to AUTH userId
	    assignment.setStaffId(staff.getUserId());

	    return staffAssignmentRepository.save(assignment);
	}
	
	@GetMapping("/students")
	public List<StudentDetails> getAllStudents() {
	    return studentService.getAllStudents();
	}
	
	@GetMapping("/assign/staff/{staffId}")
	public List<StaffAssignment>getAssignmentForStaff(@PathVariable Long staffId){
	  
		return staffAssignmentRepository.findByStaffId(staffId);
	}
	
	@GetMapping("/assign/all")
	public List<StaffAssignment>getAllAssignments()
	{
		return staffAssignmentRepository.findAll();
		}

	@DeleteMapping("/assign/{id}")
	public String deleteAssignment(@PathVariable Long id) {
	    if (staffAssignmentRepository.existsById(id)) {
	        staffAssignmentRepository.deleteById(id);
	        return "Assignment deleted successfully!";
	    } else {
	        return "Assignment not found!";
	    }
	}
	
	@GetMapping("/assign/class")
	public List<StaffAssignment> getAssignmentsForClass( @RequestParam String course,@RequestParam String section , @RequestParam Integer semester)
	{
		return staffAssignmentRepository.findByCourseAndSectionAndSemester(course,section,semester);
	}
	
	
	@GetMapping("/books/all")
	public List<Book> getAllBooksForAdmin()
	{
		return bookRepository.findAll();
	}
	
	@GetMapping("/books/staff/{staffId}")
	public List<Book>getBooksByStaff(@PathVariable Long staffId)
	{
		return bookRepository.findByUploadedByStaffId(staffId);
	}
	@GetMapping("/books/class")
    public List<Book> getBooksForClass(@RequestParam String course,
                                       @RequestParam String section,
                                       @RequestParam Integer semester) {
        return bookRepository.findByCourseAndSectionAndSemester(course, section, semester);
    }

	
}
