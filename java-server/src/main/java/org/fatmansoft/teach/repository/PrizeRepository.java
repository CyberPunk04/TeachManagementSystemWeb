package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Prize.Prize;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrizeRepository extends JpaRepository<Prize,Integer> {
    @Query(value = "select max(prizeId) from Prize  ")
    Integer getMaxId();
    List<Prize> findByStudentStudentId(Integer studentId);
    Optional<Prize> findByPrizeName(String prizeName);
    Optional<Student> findByPrizeId(Integer prizeId);
    @Query(value = "from Prize where ?1='' or prizeName like %?1% or student.person.name like %?1%  or student.person.num like %?1%")
    List<Prize> findPrizeListByNumName(String numName);
}
