package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Competition 竞赛经历表实体类  保存竞赛经历的基本信息，
 * Integer competitionId 竞赛经历表 competition 主键 competition_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * Date competitionTime 竞赛时间
 * String competitionName 竞赛名称
 * String competitionLevel 竞赛级别
 * String competitionPrize 竞赛获奖情况
 */
@Entity
@Table(	name = "competition",
        uniqueConstraints = {
        })
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competitionId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String competitionTime;
    @Size(max = 50)
    private String competitionName;
    @Size(max = 50)
    private String competitionLevel;
    @Size(max = 50)
    private String competitionPrize;

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getCompetitionTime() {
        return competitionTime;
    }

    public void setCompetitionTime(String competitionTime) {
        this.competitionTime = competitionTime;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getCompetitionLevel() {
        return competitionLevel;
    }

    public void setCompetitionLevel(String competitionLevel) {
        this.competitionLevel = competitionLevel;
    }

    public String getCompetitionPrize() {
        return competitionPrize;
    }

    public void setCompetitionPrize(String competitionPrize) {
        this.competitionPrize = competitionPrize;
    }
}
