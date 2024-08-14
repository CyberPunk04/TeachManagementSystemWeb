package org.fatmansoft.teach.models.teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;

import javax.persistence.*;

@Entity
@Table(	name = "course_selection",
        uniqueConstraints = {
        })
public class CourseSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseSelectionId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;




    public Integer getCourseSelectionId() {
        return courseSelectionId;
    }

    public void setCourseSelectionId(Integer courseSelectionId) {
        this.courseSelectionId = courseSelectionId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }



}
