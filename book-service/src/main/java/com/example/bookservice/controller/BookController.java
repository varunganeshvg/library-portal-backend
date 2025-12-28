package com.example.bookservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.bookservice.model.StaffDetails;
import com.example.bookservice.repository.StaffDetailsRepository;
import org.springframework.security.core.Authentication;

@CrossOrigin(origins = "http://127.0.0.1:5500")

@RestController
@RequestMapping("/api/books")
public class BookController {  
	
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private StaffDetailsRepository staffDetailsRepository;
	

	@GetMapping("/ping")
	
    public String ping() {
        return "book-service is alive ";
    }

	@PostMapping("/add")
	public Book addBook(@RequestBody Book book, Authentication authentication)
	{
	    String email = authentication.getName();

	    StaffDetails staff = staffDetailsRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Staff not found"));

	    // 🔑 THIS IS THE KEY LINE
	    book.setUploadedByStaffId(staff.getUserId());

	    return bookRepository.save(book);
	}
	
	@GetMapping("/list")
    public List<Book> getAllBooks() {
       return bookRepository.findAll();
    }
    
    @PutMapping("/update/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setCategory(updatedBook.getCategory());
            existingBook.setDownloadLink(updatedBook.getDownloadLink());
            return bookRepository.save(existingBook);
        } else {
            throw new RuntimeException("Book not found with ID: " + id);
        }
    }
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return "Book deleted successfully!";
        } else {
            return "Book not found!";
        }
    }
    
    @GetMapping("/search/title")
    public List<Book>searchByTitle(@RequestParam String query)
    {
    	return bookRepository.findByTitleContainingIgnoreCase(query);
    }
    
    @GetMapping("/search/author")
    public List<Book> searchByAuthor(@RequestParam String query)
    {
    	return bookRepository.findByAuthorContainingIgnoreCase(query);
    }
    
    @GetMapping("/search/category")
    public List<Book> searchByCategory(@RequestParam String query)
    {
    	return bookRepository.findByCategoryIgnoreCase(query);
    }
    
    @GetMapping("/search")
    public List<Book> searchAll(@RequestParam String query)
    {
    	return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(query, query, query);
    }
    
    @GetMapping("/class")
    public List<Book> getBooksForClass(
            @RequestParam String course,
            @RequestParam String section,
            @RequestParam Integer semester) {
        return bookRepository.findByCourseAndSectionAndSemester(course, section, semester);
    }
}