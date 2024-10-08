package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Internship;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InternshipRepository extends JpaRepository<Internship,Integer> {
    @Query(value = "select max(internshipId) from Internship  ")
    Integer getMaxId();
    List<Internship> findByStudentStudentId(Integer studentId);
    Optional<Internship> findByCompany(String company);
    Optional<Student> findByInternshipId(Integer internshipId);
    @Query(value = "from Internship where ?1='' or company like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Internship> findInternshipListByNumName(String numName);

    @Query(value = "from Internship where student.studentId like ?1 ")
    List<Internship> findByStudentId(Integer studentId);

}
