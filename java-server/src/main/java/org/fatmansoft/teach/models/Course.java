package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Course 课程表实体类  保存课程的的基本信息信息，
 * Integer courseId 人员表 course 主键 course_id
 * String num 课程编号
 * String name 课程名称
 * Integer credit 学分
 * Course preCourse 前序课程 pre_course_id 关联前序课程的主键 course_id
 */
@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    @NotBlank
    @Size(max = 20)
    private String num;

    @Size(max = 50)
    private String name;
    private Integer credit;
    @ManyToOne
    @JoinColumn(name="pre_course_id")
    private Course preCourse;
    @Size(max = 12)
    private String coursePath;

    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
    private String classTime;
    private Integer startWeek;
    private Integer endWeek;
    private String type;
    private Integer much;
    private String place;
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public Integer getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(Integer startWeek) {
        this.startWeek = startWeek;
    }

    public Integer getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(Integer endWeek) {
        this.endWeek = endWeek;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMuch() {
        return much;
    }

    public void setMuch(Integer much) {
        this.much = much;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }



    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Course getPreCourse() {
        return preCourse;
    }

    public void setPreCourse(Course preCourse) {
        this.preCourse = preCourse;
    }

    public String getCoursePath() {
        return coursePath;
    }

    public void setCoursePath(String coursePath) {
        this.coursePath = coursePath;
    }
}
