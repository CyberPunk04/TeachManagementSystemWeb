package org.fatmansoft.teach.repository.teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.teaching.TeacherPower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherPowerRepository extends JpaRepository<TeacherPower,Integer> {
    @Query(value = "from TeacherPower where teacherPowerId like ?1")
    Optional<TeacherPower> findByNum(Integer num);

}