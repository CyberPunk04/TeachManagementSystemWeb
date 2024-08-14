package org.fatmansoft.teach.controllers;

import org.apache.poi.ss.formula.functions.T;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.teaching.CourseSelectionRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")

public class CourseController {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    @PostMapping("/getCourseListSerializable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StreamingResponseBody> getCourseListSerializable(@Valid @RequestBody DataRequest dataRequest) {
        try {
            List<Course> cList = courseRepository.findAll();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(out);
                oo.writeObject(cList);
                oo.close();
                byte [] data = out.toByteArray();
                MediaType mType = new MediaType(MediaType.APPLICATION_OCTET_STREAM);
                StreamingResponseBody stream = outputStream -> {
                    outputStream.write(data);
                };
                return ResponseEntity.ok()
                        .contentType(mType)
                        .body(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/getCourseList")
    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        if(numName == null)
            numName = "";
        List<Course> cList = courseRepository.findCourseListByNumName(numName);//数据库查询操作
        List dataList = new ArrayList();
        Map m;
        Course pc;
        Teacher t;
        for (Course c : cList) {
            List<CourseSelection>  choose = courseSelectionRepository.findByCourseId(c.getCourseId());
            Integer last = c.getMuch()- choose.size();
            if(last<0)
                last = 0;
            m = new HashMap();
            m.put("courseId", c.getCourseId()+"");
            m.put("num",c.getNum());
            m.put("name",c.getName());
            m.put("credit",c.getCredit());
            m.put("coursePath",c.getCoursePath());
            pc =c.getPreCourse();
            if(pc != null) {
                m.put("preCourse",pc.getName());
                m.put("preCourseId",pc.getCourseId());
            }
            t = c.getTeacher();
            if (t != null){
                m.put("teacher",t.getPerson().getName());
                m.put("teacherId",t.getTeacherId());
            }
            m.put("classTime",c.getClassTime());
            m.put("startWeek",c.getStartWeek());
            m.put("endWeek",c.getEndWeek());
            m.put("type",c.getType());
            m.put("last",last);
            m.put("much",c.getMuch());
            m.put("place",c.getPlace());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/courseSave")
    public DataResponse courseSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        String num = dataRequest.getString("num");
        String name = dataRequest.getString("name");
        String coursePath = dataRequest.getString("coursePath");
        Integer credit = dataRequest.getInteger("credit");
        Integer preCourseId = dataRequest.getInteger("preCourseId");
        Integer teacherId = dataRequest.getInteger("teacherId");
        Integer startWeek = dataRequest.getInteger("startWeek");
        Integer endWeek = dataRequest.getInteger("endWeek");
        Integer much = dataRequest.getInteger("much");
        String place = dataRequest.getString("place");
        String classTime= dataRequest.getString("classTime");
        String type = dataRequest.getString("type");
        Optional<Course> op;
        Course c= null;

        Optional<Course> nnn = courseRepository.findByNum(num);
        if(nnn.isPresent())
            return CommonMethod.getReturnMessageError("课序号已存在，请进行检查");
        Optional<Course> aaa = courseRepository.findByName(name);
        if(aaa.isPresent())
            return CommonMethod.getReturnMessageError("课程名称已存在，请检查");
        List<Course> ccc = courseRepository.findTeacherClassTime(teacherId,classTime);
        if(!ccc.isEmpty())
            return CommonMethod.getReturnMessageError("该教师已有这一时段课程");
        List<Course> sss = courseRepository.findClassTimePlace(classTime,place);
        if(!sss.isEmpty())
            return CommonMethod.getReturnMessageError("这一时段该教师已占用");
        if(courseId != null) {
            op = courseRepository.findById(courseId);
            if(op.isPresent())
                c= op.get();
        }
        if(c== null)
            c = new Course();
        Course pc =null;
        if(preCourseId != null) {
            op = courseRepository.findById(preCourseId);
            if(op.isPresent())
                pc = op.get();
        }
        Optional<Teacher> t;
        Teacher tt = null;
        if(teacherId != null) {
            t = teacherRepository.findById(teacherId);
            if(t.isPresent())
                tt = t.get();
        }
        c.setNum(num);
        c.setName(name);
        c.setCredit(credit);
        c.setCoursePath(coursePath);
        c.setPreCourse(pc);
        c.setTeacher(tt);
        c.setMuch(much);
        c.setPlace(place);
        c.setType(type);
        c.setClassTime(classTime);
        c.setStartWeek(startWeek);
        c.setEndWeek(endWeek);
        courseRepository.save(c);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/courseDelete")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Optional<Course> op;
        Course c= null;
        if(courseId != null) {
            op = courseRepository.findById(courseId);
            if(op.isPresent()) {
                c = op.get();
                courseRepository.delete(c);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }
}
