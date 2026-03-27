package com.encrytion.jwe.controller;

import com.encrytion.jwe.domain.Student;
import com.encrytion.jwe.dto.EncryptedRequest;
import com.encrytion.jwe.dto.StudentIdRequest;
import com.encrytion.jwe.service.JweService;
import com.encrytion.jwe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/students/secure")
public class SecureStudentController {

    private final StudentService studentService;
    private final JweService jweService;

    @PostMapping
    public Student saveStudent(@RequestBody EncryptedRequest encryptedRequest) {
        Student student = jweService.decrypt(encryptedRequest.jwe(), Student.class);
        return studentService.createStudent(student);
    }

    @PostMapping("/by-id")
    public Student getById(@RequestBody EncryptedRequest encryptedRequest) {
        StudentIdRequest request = jweService.decrypt(encryptedRequest.jwe(), StudentIdRequest.class);
        return studentService.getStudentById(request.id());
    }

    @GetMapping("/{id}")
    public Student getByIdPath(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }
}
