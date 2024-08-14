package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Internship 校外实习表实体类  保存校外实习的基本信息，
 * Integer internshipId 竞赛经历表 internship 主键 internship_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String company 实习公司
 * Date startTime 实习开始时间
 * String internshipPosition 实习职务
 * String length 实习时长
 */
@Entity
@Table(	name = "internship",
        uniqueConstraints = {
        })
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer internshipId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @Size(max = 50)
    private String company;
    private String startTime;
    @Size(max = 20)
    private String internshipPosition;
    @Size(max = 20)
    private String length;

    public Integer getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(Integer internshipId) {
        this.internshipId = internshipId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getInternshipPosition() {
        return internshipPosition;
    }

    public void setInternshipPosition(String internshipPosition) {
        this.internshipPosition = internshipPosition;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}

