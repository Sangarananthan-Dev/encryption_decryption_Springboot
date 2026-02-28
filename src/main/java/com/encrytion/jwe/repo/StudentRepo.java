package com.encrytion.jwe.repo;

import com.encrytion.jwe.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student , Long> {
    Student findByName(String name);
}
