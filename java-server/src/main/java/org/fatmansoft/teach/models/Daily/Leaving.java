package org.fatmansoft.teach.models.Daily;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Leaving 请假表实体类  保存请假的基本信息，
 * Integer leavingId 请假表 leaving 主键 leaving_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String campus 校区
 * String destination 目的地
 * String reason 请假原因
 * Date leavingTime 请假时间
 * Date startTime 开始时间
 * Date endTime 结束时间
 */
@Entity
@Table(	name = "leaving",
        uniqueConstraints = {
        })
public class Leaving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leavingId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Size(max = 20)
    private String campus;
    @Size(max = 50)
    private String destination;
    @Size(max = 50)
    private String reason;
    private String leavingTime;
    private String startTime;
    private String endTime;
    private String approval;

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;

    public Integer getLeavingId() {
        return leavingId;
    }

    public void setLeavingId(Integer leavingId) {
        this.leavingId = leavingId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(String leavingTime) {
        this.leavingTime = leavingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}


