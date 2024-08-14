package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Practice 实践活动表实体类  保存实践活动的基本信息，
 * Integer practiceId 实践活动表 practice 主键 practice_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String practiceTime 实践活动时间
 * String practiceName 实践活动名称
 * String practiceContent 实践活动内容
 */
@Entity
@Table(	name = "practice",
        uniqueConstraints = {
        })
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer practiceId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String practiceTime;
    @Size(max = 50)
    private String practiceName;

    private String practiceContent;

    public Integer getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(Integer practiceId) {
        this.practiceId = practiceId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPracticeTime() {
        return practiceTime;
    }

    public void setPracticeTime(String practiceTime) {
        this.practiceTime = practiceTime;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getPracticeContent() {
        return practiceContent;
    }

    public void setPracticeContent(String practiceContent) {
        this.practiceContent = practiceContent;
    }
}
