package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.StudentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/student/{email}")
	public StudentDTO getStudent(@PathVariable("email") String email) {
		Student checkStudent = studentRepository.findByEmail(email);
		if(checkStudent != null) {
			return createStudentDTO(checkStudent);
		}
		
		throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student does not exist");
	}
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent(@RequestBody StudentDTO incomingStudent) {
		if(incomingStudent.email == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student needs an email address");
		}
		
		// Check if email exists
		Student checkStudent = studentRepository.findByEmail(incomingStudent.email);
		
		if(checkStudent != null) {
			StudentDTO ns = createStudentDTO(checkStudent);
			return ns;
		} else {
			Student newStudent = new Student();
			
			newStudent.setName(incomingStudent.name);
			newStudent.setEmail(incomingStudent.email);
			newStudent.setStatus(incomingStudent.status);
			newStudent.setStatusCode(incomingStudent.statusCode);
			
			studentRepository.save(newStudent);
		}
		
		return incomingStudent;
	}
	
	@PatchMapping("/student/{email}/hold")
	public StudentDTO updateStudentStatus(@PathVariable("email") String email) {
		Student foundStudent = studentRepository.findByEmail(email);
		if(foundStudent == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found, yo!");
		} else {
			foundStudent.setStatus("HOLD");
			foundStudent.setStatusCode(100);
			
			studentRepository.save(foundStudent);
		}
		return createStudentDTO(foundStudent);
	}
	
	@PatchMapping("/student/{email}/remove_hold")
	public StudentDTO clearStudentStatus(@PathVariable("email") String email) {
		Student foundStudent = studentRepository.findByEmail(email);
		
		if(foundStudent == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found");
		} else {
			foundStudent.setStatus(null);
			foundStudent.setStatusCode(0);
			studentRepository.save(foundStudent);
		}
		
		return createStudentDTO(foundStudent);
	}
	
	public StudentDTO createStudentDTO(Student student) {
		StudentDTO newStudent = new StudentDTO();
		
		newStudent.student_id = student.getStudent_id();
		newStudent.name = student.getName();
		newStudent.email = student.getEmail();
		newStudent.statusCode = student.getStatusCode();
		newStudent.status = student.getStatus();
		
		return newStudent;
	}
}
