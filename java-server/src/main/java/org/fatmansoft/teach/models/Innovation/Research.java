package org.fatmansoft.teach.models.Innovation;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Research 科研项目表实体类  保存科研项目的基本信息，
 * Integer researchId 科研项目表 research 主键 research_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * Date researchTime 科研成果发表时间
 * String researchName 科研项目名称
 * String researchContent 科研项目内容
 */
@Entity
@Table(	name = "research",
        uniqueConstraints = {
        })
public class Research {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer researchId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String researchTime;
    @Size(max = 50)
    private String researchName;

    private String researchContent;

    public Integer getResearchId() {
        return researchId;
    }

    public void setResearchId(Integer researchId) {
        this.researchId = researchId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getResearchTime() {
        return researchTime;
    }

    public void setResearchTime(String researchTime) {
        this.researchTime = researchTime;
    }

    public String getResearchName() {
        return researchName;
    }

    public void setResearchName(String researchName) {
        this.researchName = researchName;
    }

    public String getResearchContent() {
        return researchContent;
    }

    public void setResearchContent(String researchContent) {
        this.researchContent = researchContent;
    }
}
