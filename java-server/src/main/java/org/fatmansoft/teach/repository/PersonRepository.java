package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Person 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  person 表中的最大的person_id;    JPQL 注解
 * Optional<Person> findByNum(String num);  根据num查询获得Option<Person>对象,  命名规范
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByNum(String num);
    @Query(value = "select max(personId) from Person  ")
    Integer getMaxId();
    //Optional<Person> findByName(String name);

    //根据用户姓名查询，如果没有输入，返回所有用户

    @Query("SELECT p FROM Person p WHERE (:name is null or p.name LIKE %:name%)")
    Optional<Person> findByName(String name);

    @Query(value = "from Person where name like ?1 and num like ?2")
    Optional<Person> findByNameAndNum(String student,String studentNum);
}
