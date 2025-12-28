package com.example.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.model.User;
import com.example.authservice.repository.Userrepository;

@Service
public class Userservice {

	@Autowired
	private Userrepository userrepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public User registerUser(User user)
	{
	    // 1️⃣ Check for existing email
	    if (userrepository.findByEmail(user.getEmail()) != null) {
	        throw new RuntimeException("Email already registered");
	    }

	    // 2️⃣ Validate role
	    if (user.getRole() == null || user.getRole().isBlank()) {
	        throw new RuntimeException("Role is required");
	    }

	    // 3️⃣ Convert role into ROLE_ format
	    String formattedRole = user.getRole().trim().toUpperCase(); // e.g. "staff" -> "STAFF"
	    if (!formattedRole.startsWith("ROLE_")) {
	        formattedRole = "ROLE_" + formattedRole;  // -> ROLE_STAFF
	    }
	    user.setRole(formattedRole);

	    // 4️⃣ Encrypt password
	    user.setPassword(passwordEncoder.encode(user.getPassword()));

	    // 5️⃣ Save user
	    return userrepository.save(user);
	}
	
                                             
   public User loginuser(String email,String password)
   {
	   User existinguser= userrepository.findByEmail(email);
	   
	   if(existinguser == null)
	   {
		 throw new RuntimeException("user not found");
	   }
	   
	   //if(!existinguser.getPassword().equals(password))
	   //{   
		 //  throw new RuntimeException("invalid password");
       //}
	  if(!passwordEncoder.matches(password, existinguser.getPassword()))
	  {
		  throw new RuntimeException("password not valid");
	  }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
       
   
   return existinguser;
   
   }
   
   public User getUserByEmail(String email) {
	    User user = userrepository.findByEmail(email);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }
	    return user;
	}
}
	

