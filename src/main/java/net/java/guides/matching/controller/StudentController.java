package net.java.guides.matching.controller;

import net.java.guides.matching.dto.StudentRegistrationDTO;
import net.java.guides.matching.entity.Student;
import net.java.guides.matching.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<Student> register(@RequestBody StudentRegistrationDTO dto) {
        Student student = studentService.registerStudent(dto);
        return ResponseEntity.ok(student);
    }
}
