package org.fatmansoft.teach.models.teaching;

import javax.persistence.*;

@Entity
@Table(	name = "teacher_power",
        uniqueConstraints = {
        })
public class TeacherPower {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer teacherPowerId;

        private Integer power;

        public Integer getTeacherPowerId() {
                return teacherPowerId;
        }

        public void setTeacherPowerId(Integer teacherPowerId) {
                this.teacherPowerId = teacherPowerId;
        }

        public Integer getPower() {
                return power;
        }

        public void setPower(Integer power) {
                this.power = power;
        }



}
