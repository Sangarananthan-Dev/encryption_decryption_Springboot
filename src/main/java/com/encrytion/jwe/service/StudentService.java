package com.encrytion.jwe.service;

import com.encrytion.jwe.domain.Student;
import com.encrytion.jwe.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo repo;

    public Student createStudent(Student student){
        return repo.save(student);
    }

    public Student getStudentById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<Student> getAllStudents(){
        return repo.findAll();
    }

    public Student searchStudent(String searchString){
        return repo.findByName((searchString));
    }
}
