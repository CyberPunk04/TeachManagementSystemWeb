package org.fatmansoft.teach.models.Daily;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Activity 日常活动表实体类  保存日常活动的基本信息，
 * Integer activityId 日常活动表 activity 主键 activity_id
 * Student student 关联学生 student_id 关联学生的主键 studentId
 * String activityName 活动名称
 * String activityType 活动类型
 * Date activityTime 活动时间
 * String activityLocation 活动地点
 */
@Entity
@Table(	name = "activity",
        uniqueConstraints = {
        })
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    private Integer studentId;


    @Size(max = 50)
    private String activityName;
    @Size(max = 50)
    private String activityType;
    private String activityTime;


    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }


    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    private String activityLocation;


    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
