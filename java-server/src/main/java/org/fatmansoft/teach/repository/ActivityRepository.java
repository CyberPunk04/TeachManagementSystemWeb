package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Daily.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Activity 数据操作接口，主要实现Activity数据的查询操作
 * Integer getMaxId()  Activity 表中的最大的activity_id;    JPQL 注解
 * Optional<Activity> findByStudentIdAndDay(Integer studentId, String day);  根据student_id 和day 查询获得Option<Activity>对象,  命名规范
 * List<Activity> findListByStudent(Integer studentId);  查询学生（student_id）所有的消费记录  JPQL 注解
 */
public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    @Query(value = "select max(activityId) from Activity  ")
    Integer getMaxId();
    List<Activity> findByStudentId(Integer studentId);

    Optional<Activity> findByActivityId(Integer activityId);


    @Query(value= "from Activity where ?1 =0 or studentId=?1 order by activityId")
    List<Activity> findListByStudent(Integer studentId);
}
