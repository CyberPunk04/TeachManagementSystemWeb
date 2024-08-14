package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostPostId(Integer postId);


    // 统计评论数量
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
    int countByPostId(@Param("postId") Integer postId);

}
