package org.fatmansoft.teach.repository.teaching;

import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseSelectionRepository extends JpaRepository<CourseSelection,Integer> {
    @Query(value = "select max(courseSelectionId) from CourseSelection  ")
    Integer getMaxId();

    @Query(value = "from CourseSelection where student.studentId like ?1 and course.courseId like ?2")
    Optional<CourseSelection> findByStudentStudentIdAndCourseCourseId(Integer studentId, Integer courseId);
    @Query(value = "from CourseSelection where (?1='' or student.person.name like %?1% or course.name like %?1%) and course.teacher.teacherId like ?2 order by course.name")
    List<CourseSelection> findCourseSelectionListByNumNameAndTeacher(String numName, Integer teacherId);

    @Query(value = "from CourseSelection where student.studentId like ?1 and course.classTime like %?2%")
    Optional<CourseSelection> findByStudentStudentIdAndCourseClassTime(Integer studentId,String classTime);
    @Query(value = "from CourseSelection where student.studentId like ?1 and course.classTime like %?2%")
    List<CourseSelection> findByStudentStudentIdCourseClassTime(Integer studentId,String classTime);

    @Query(value = "from CourseSelection where student.studentId like ?1")
    List<CourseSelection> findByStudentId(Integer studentId);

    @Query(value = "from CourseSelection where course.courseId like ?1")
    List<CourseSelection> findByCourseId(Integer courseId);

    @Query(value = "from CourseSelection where course.teacher.teacherId like ?2 ")
    List<CourseSelection> findCourseSelectionListByTeacher(Integer teacherId);
}
