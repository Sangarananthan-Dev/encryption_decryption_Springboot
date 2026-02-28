package com.encrytion.jwe.controller;

import com.encrytion.jwe.domain.Student;
import com.encrytion.jwe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService service;

    @GetMapping
    public List<Student> getAll(){
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id){
        return service.getStudentById(id);
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student){
        return service.createStudent(student);
    }

    @GetMapping("/search")
    public Student searchStudent(@RequestParam String searchString){
        return service.searchStudent(searchString);
    }

}
