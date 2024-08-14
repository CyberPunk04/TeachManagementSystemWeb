package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Daily.Leaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Leaving 数据操作接口，主要实现Leaving数据的查询操作
 * Integer getMaxId()  Leaving 表中的最大的leaving_Id;    JPQL 注解
 * Optional<Leaving> findByLeavingId(Integer leavingId); 根据关联的Leaving的leavingId查询获得Option<Leaving>对象 命名规范
 * List<Leaving> findStudentListByNumName(String numName); 根据输入的参数 如果参数为空，查询所有的学生，输入参数不为空，查询学号或姓名包含输入参数串的所有学生请假集合
 */

public interface LeavingRepository extends JpaRepository<Leaving,Integer> {
    @Query(value = "select max(leavingId) from Leaving  ")
    Integer getMaxId();
    List<Leaving> findByStudentStudentId(Integer studentId);
    Optional<Leaving> findByLeavingId(Integer leavingId);


    @Query(value= "from Leaving where ?1 =0 or student.studentId=?1 order by leavingId")
    List<Leaving> findListByStudent(Integer studentId);
    @Query(value = "from Leaving where (?1='' or campus like %?1% or destination like %?1%  or reason like %?1%) and (student.studentId like ?2)")
    List<Leaving> findLeavingListByNumNameAndStudentId(String numName, Integer studentId);

}
