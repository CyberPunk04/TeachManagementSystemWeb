package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikesRepository extends JpaRepository<PostLikes, Integer> {

    //根据post_id统计点赞数
    @Query("SELECT COUNT(p) FROM PostLikes p WHERE p.post.postId = :postId")
    int countByPostId(@Param("postId") Integer postId);


    //根据post_id和person_id查询是否已点赞
    @Query("SELECT p FROM PostLikes p WHERE p.post.postId = :postId AND p.person.personId = :personId")
    PostLikes findByPostIdAndPersonId(@Param("postId") Integer postId, @Param("personId") Integer personId);
}
