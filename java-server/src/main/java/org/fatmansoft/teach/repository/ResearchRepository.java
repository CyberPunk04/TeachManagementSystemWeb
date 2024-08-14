package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Research;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResearchRepository extends JpaRepository<Research,Integer> {
    @Query(value = "select max(researchId) from Research ")
    Integer getMaxId();
    List<Research> findByStudentStudentId(Integer studentId);
    Optional<Research> findByResearchName(String researchName);
    Optional<Student> findByResearchId(Integer researchId);
    @Query(value = "from Research where ?1='' or researchName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Research> findResearchListByNumName(String numName);
    @Query(value = "from Research where student.studentId like ?1")
    List<Research> findByStudentId(Integer studentId);
}
