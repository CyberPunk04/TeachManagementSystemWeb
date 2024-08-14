package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Lecture 培训讲座表实体类  保存参加培训讲座的基本信息，
 * Integer Id Lecture 培训讲座表 lecture 主键 lecture_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * Date lectureTime 讲座时间
 * String lectureName 讲座名称
 * String lecturePosition 讲座地点
 * String lectureLeader 主讲人
 */
@Entity
@Table(	name = "lecture",
        uniqueConstraints = {
        })
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lectureId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String lectureTime;
    @Size(max = 50)
    private String lectureName;
    @Size(max = 50)
    private String lecturePosition;
    @Size(max = 20)
    private String lectureLeader;

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(String lectureTime) {
        this.lectureTime = lectureTime;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLecturePosition() {
        return lecturePosition;
    }

    public void setLecturePosition(String lecturePosition) {
        this.lecturePosition = lecturePosition;
    }

    public String getLectureLeader() {
        return lectureLeader;
    }

    public void setLectureLeader(String lectureLeader) {
        this.lectureLeader = lectureLeader;
    }
}
