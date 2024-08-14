package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Practice;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PracticeRepository extends JpaRepository<Practice,Integer> {

    @Query(value = "select max(practiceId) from Practice  ")
    Integer getMaxId();

    List<Practice> findByStudentStudentId(Integer studentId);
    Optional<Practice> findByPracticeName(String practiceName);
    Optional<Student> findByPracticeId(Integer practiceId);
    @Query(value = "from Practice where ?1='' or practiceName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Practice> findPracticeListByNumName(String numName);
    @Query(value = "from Practice where student.studentId like ?1")
    List<Practice> findByStudentId(Integer studentId);
}
