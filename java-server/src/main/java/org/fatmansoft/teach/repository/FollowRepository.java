package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Follow;
import org.fatmansoft.teach.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {


    /** 查询关注列表
    * @Param followerID 关注者ID
     * @return 关注的人列表
     */
    @Query("SELECT f.following FROM Follow f WHERE f.follower.personId = :followerID")
    List<Person> findFollowingByFollower(@Param("followerID") Integer followerID);

    /** 查询是否已关注
    * @Param followerID 关注者ID
    * @Param followingID 被关注者ID
    * @return 如果有关注关系，返回这条记录，否则返回null
    **/
    @Query("SELECT f FROM Follow f WHERE f.follower.personId = :followerID AND f.following.personId = :followingID")
    Follow findByFollowerAndFollowing(@Param("followerID") Integer followerID, @Param("followingID") Integer followingID);

    /**统计粉丝数量
     *
     * @param personId
     * @return
     */
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.personId = :personId")
    int countByFollowing(@Param("personId") Integer personId);


    /**
     * 返回互相关注的人
     */
    @Query("SELECT f.following FROM Follow f WHERE f.follower.personId = :personId AND f.following IN (SELECT f2.follower FROM Follow f2 WHERE f2.following.personId = :personId)")
    List<Person> findMutualFollowing(@Param("personId") Integer personId);

}
