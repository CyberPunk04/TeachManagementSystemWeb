package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Innovation;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InnovationRepository extends JpaRepository<Innovation,Integer> {
    @Query(value = "select max(innovationId) from Innovation  ")
    Integer getMaxId();
    List<Innovation> findByStudentStudentId(Integer studentId);
    Optional<Innovation> findByInnovationName(String innovationName);
    Optional<Student> findByInnovationId(Integer innovationId);
    @Query(value = "from Innovation where ?1='' or innovationName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Innovation> findInnovationListByNumName(String numName);
    @Query(value = "from Innovation where student.studentId = ?1")
    List<Innovation> findByStudentId(Integer studentId);
}
