package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Innovation 创新项目表实体类  保存参加创新项目的基本信息，
 * Integer Id Innovation 创新项目表 innovation 主键 innovation_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String innovationName 创新项目名称
 * Date innovationTime 创新项目时间
 * String teamName 创新团队名称
 * Integer teamNum 创新团队人数
 * String teamPosition 队内职务
 * String innovationContent 创新成果
 */
@Entity
@Table(	name = "innovation",
        uniqueConstraints = {
        })
public class Innovation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer innovationId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Size(max = 50)
    private String innovationName;
    private String innovationTime;
    @Size(max = 50)
    private String teamName;
    private Integer teamNum;
    @Size(max = 20)
    private String teamPosition;

    private String innovationContent;

    public Integer getInnovationId() {
        return innovationId;
    }

    public void setInnovationId(Integer innovationId) {
        this.innovationId = innovationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getInnovationName() {
        return innovationName;
    }

    public void setInnovationName(String innovationName) {
        this.innovationName = innovationName;
    }

    public String getInnovationTime() {
        return innovationTime;
    }

    public void setInnovationTime(String innovationTime) {
        this.innovationTime = innovationTime;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(Integer teamNum) {
        this.teamNum = teamNum;
    }

    public String getTeamPosition() {
        return teamPosition;
    }

    public void setTeamPosition(String teamPosition) {
        this.teamPosition = teamPosition;
    }

    public String getInnovationContent() {
        return innovationContent;
    }

    public void setInnovationContent(String innovationContent) {
        this.innovationContent = innovationContent;
    }
}
