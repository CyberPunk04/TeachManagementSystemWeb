package org.fatmansoft.teach.models.Prize;

import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Prize 荣誉表实体类  保存获得荣誉的基本信息，
 * Integer prizeId 荣誉表 prize 主键 prize_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * Date gotTime 荣誉获得时间
 * String prizeName 荣誉名称
 * String prizeType 荣誉类型
 * String prizeLevel 荣誉级别
 */
@Entity
@Table(	name = "prize")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prizeId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String gotTime;
    @Size(max = 50)
    private String prizeName;
    @Size(max = 50)
    private String prizeType;
    @Size(max = 50)
    private String prizeLevel;
    //@Size(max = 20)
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getGotTime() {
        return gotTime;
    }

    public void setGotTime(String gotTime) {
        this.gotTime = gotTime;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeLevel() {
        return prizeLevel;
    }

    public void setPrizeLevel(String prizeLevel) {
        this.prizeLevel = prizeLevel;
    }
}
