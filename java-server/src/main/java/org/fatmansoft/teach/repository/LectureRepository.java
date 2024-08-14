package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Innovation.Lecture;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Integer> {
    @Query(value = "select max(lectureId) from Lecture  ")
    Integer getMaxId();
    List<Lecture> findByStudentStudentId(Integer studentId);
    Optional<Lecture> findByLectureName(String lectureName);
    Optional<Student> findByLectureId(Integer lectureId);
    @Query(value = "from Lecture where ?1='' or lectureName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Lecture> findLectureListByNumName(String numName);
}