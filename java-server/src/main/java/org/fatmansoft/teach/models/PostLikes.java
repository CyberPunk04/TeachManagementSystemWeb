package org.fatmansoft.teach.models;

import javax.persistence.*;

@Entity
@Table(name = "post_likes")
public class PostLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_likes_id")
    private Integer postLikesId;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;//关联到post表的postId，作为点赞的帖子

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;//关联到person表的personId，作为点赞人

    public Integer getPostLikesId() {
        return postLikesId;
    }

    public void setPostLikesId(Integer postLikesId) {
        this.postLikesId = postLikesId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
