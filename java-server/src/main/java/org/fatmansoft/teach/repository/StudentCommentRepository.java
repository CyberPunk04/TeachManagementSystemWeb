package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Post;
import org.fatmansoft.teach.models.StudentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface StudentCommentRepository  extends JpaRepository<StudentComment,Integer> {

    //根据被评论者查询所有评价如果没有被评论者就返回所有评论
    @Query("From StudentComment sc WHERE (:commentedId IS NULL OR sc.commented.studentId = :commentedId)")
    List<StudentComment> findByCommentedId(Integer commentedId);

    //根据评论者的学号和姓名查询某学生发的评论
    @Query("FROM StudentComment sc WHERE (:searchTerm IS NULL OR sc.commenter.person.num LIKE %:searchTerm% OR sc.commenter.person.name  LIKE %:searchTerm%)")
    List<StudentComment> findByNumName(@Param("searchTerm") String numName);

    //根据评论者和被评论者的Id查询有无该记录
    @Query("FROM StudentComment sc WHERE (sc.commenter.studentId = :commenterId AND sc.commented.studentId = :commentedId )")
    Optional<StudentComment> findByCommenterIdCommentedId(@Param("commenterId")Integer commenterId, @Param("commentedId") Integer commentedId);
}
