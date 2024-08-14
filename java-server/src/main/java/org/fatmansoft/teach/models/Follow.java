package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer followId;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private Person follower;//关联到person表的personId，作为关注人

    @ManyToOne
    @JoinColumn(name="following_id")
    private Person following;//关联到person表的personId，作为被关注人

    public Integer getFollowId() {
        return followId;
    }

    public void setFollowId(Integer followId) {
        this.followId = followId;
    }

    public Person getFollower() {
        return follower;
    }

    public void setFollower(Person follower) {
        this.follower = follower;
    }

    public Person getFollowing() {
        return following;
    }

    public void setFollowing(Person following) {
        this.following = following;
    }
}
