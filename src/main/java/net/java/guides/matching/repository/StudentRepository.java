package net.java.guides.matching.repository;

import net.java.guides.matching.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserEmail(String email);
}
