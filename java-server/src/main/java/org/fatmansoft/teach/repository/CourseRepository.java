package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course 数据操作接口，主要实现Course数据的查询操作
 */

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "select max(courseId) from Course  ")
    Integer getMaxId();

    Optional<Course> findByName(String name);
    List<Course> findByTeacherTeacherId(Integer teacherId);

    Optional<Course> findByCourseId(Integer courseId);
    @Query(value = "from Course where ?1='' or name like %?1% or num like %?1% ")
    List<Course> findCourseListByNumName(String numName);
    Optional<Course> findByNum(String num);
    @Query(value = "from Course where teacher.teacherId like ?1 ")
    List<Course> findByTeacher(Integer teacherId);

    @Query(value = "from Course where teacher.teacherId like ?1 and classTime like %?2%")
    List<Course> findTeacherClassTime(Integer teacherId,String classTime);
    @Query(value = "from Course where classTime like %?1% and place like ?2")
    List<Course> findClassTimePlace(String classTime,String place);
    @Query(value = "from Course where teacher.teacherId like ?1 and (num like %?2% or name like %?2%)")
    List<Course> findByTeacherTeacherIdNumName(Integer teacherId,String numName);
}
