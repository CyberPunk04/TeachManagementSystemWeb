package org.fatmansoft.teach.models;


import javax.persistence.*;

@Entity
@Table( name = "studentComment",
        uniqueConstraints = {
        })
public class StudentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentCommentId;


    @OneToOne
    @JoinColumn(name="commented_id")
    private Student commented;

    @OneToOne
    @JoinColumn(name="commenter_id")

    private Student commenter;

    private String comment;

    private String tags;

    private Integer value;

    public Integer getStudentCommentId() {
        return studentCommentId;
    }

    public void setStudentCommentId(Integer studentCommentId) {
        this.studentCommentId = studentCommentId;
    }

    public Student getCommented() {
        return commented;
    }

    public void setCommented(Student commented) {
        this.commented = commented;
    }

    public Student getCommenter() {
        return commenter;
    }

    public void setCommenter(Student commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
