package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Competition;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition,Integer> {
    @Query(value = "select max(competitionId) from Competition  ")
    Integer getMaxId();

    Optional<Competition> findByCompetitionName(String competitionName);
    Optional<Student> findByCompetitionId(Integer competitionId);
    List<Competition> findByStudentStudentId(Integer studentId);
    @Query(value = "from Competition where ?1='' or competitionName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Competition> findCompetitionListByNumName(String numName);
    @Query(value = "from Competition where student.studentId like ?1")
    List<Competition> findByStudentId(Integer studentId);
}

