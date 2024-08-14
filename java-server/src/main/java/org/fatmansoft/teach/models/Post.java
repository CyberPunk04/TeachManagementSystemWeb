package org.fatmansoft.teach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


/*
* post表实体类
* post_id 主键
* person_id 外键
* content 内容
* created_at 创建时间
*
* */
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;


    @OneToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore
    private Person person;//关联到person表的person_id,作为发帖人

    @Column(name = "content")
    private String content;//帖子文件内容 

    @Column(name = "created_at")
    private String created_at;//发帖时间

    @Column(name = "image")
    private String image;//帖子配的图像

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Column(name = "likes")
    private Integer likes;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
