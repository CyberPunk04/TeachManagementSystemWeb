package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//关于post的数据库操作
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Override
    Optional<Post> findById(Integer integer);

    @Query("SELECT p.person FROM Post p WHERE p.postId = :postId")
    Optional<Person> findPersonByPostId(@Param("postId") Integer postId);
    List<Post> findByPersonPersonId(Integer id);


    //根据发帖人查询发了多少帖子
    @Query("SELECT COUNT(p) FROM Post p WHERE p.person.personId = :personId")
    int countPostByPersonId(@Param("personId") Integer personId);

    //根据帖子内容或者用户id查询帖子，如果没有输入，返回全部帖子
    @Query("FROM Post p WHERE (:searchTerm IS NULL OR p.content LIKE %:searchTerm% OR p.person.name LIKE %:searchTerm%)")
    List<Post> findPostListByContentOrPerson(@Param("searchTerm") String contentPersonName);


}
