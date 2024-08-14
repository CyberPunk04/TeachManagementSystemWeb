package org.fatmansoft.teach.service.Teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.teaching.CourseSelectionRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class CourseSelectionService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    public synchronized Integer getNewCourseSelectionId(){  //synchronized 同步方法
        Integer  id = courseSelectionRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }

    public Map getMapFromCourse(Course c) {
        Map m = new HashMap();
        Teacher t;
        if(c == null)
            return m;
        m.put("courseName",c.getName());
        m.put("courseNum",c.getNum());
        m.put("courseId", c.getCourseId());
        m.put("credit",c.getCredit());
        m.put("preCourseId",c.getCourseId());
        m.put("place",c.getPlace());
        m.put("classTime",c.getClassTime());
        t=c.getTeacher();
        if(t==null){
            return m;
        }
        m.put("teacher",t.getPerson().getName());
        return m;
    }

    public List getCourseMapList(String numName) {
        List dataList = new ArrayList();
        List<Course> sList =  courseRepository.findCourseListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromCourse(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getCourseMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Course c= null;
        Optional<Course> oc;
        if(courseId != null) {
            oc= courseRepository.findById(courseId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromCourse(c));
    }

    public DataResponse getCourseSelectionList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List<CourseSelection> cList = courseSelectionRepository.findByStudentId(studentId);  //数据库查询操作
        List dataList = new ArrayList();
        Map m;
        Course pc;
        Teacher t;
        for (CourseSelection c : cList) {
            m = new HashMap();
            m.put("courseId", c.getCourse().getCourseId()+"");
            m.put("num",c.getCourse().getNum());
            m.put("name",c.getCourse().getName());
            m.put("credit",c.getCourse().getCredit());
            m.put("coursePath",c.getCourse().getCoursePath());
            pc =c.getCourse().getPreCourse();
            if(pc != null) {
                m.put("preCourse",pc.getName());
                m.put("preCourseId",pc.getCourseId());
            }
            t = c.getCourse().getTeacher();
            if (t != null){
                m.put("teacher",t.getPerson().getName());
                m.put("teacherId",t.getTeacherId());
            }
            m.put("classTime",c.getCourse().getClassTime());
            m.put("startWeek",c.getCourse().getStartWeek());
            m.put("endWeek",c.getCourse().getEndWeek());
            m.put("type",c.getCourse().getType());
            m.put("much",c.getCourse().getMuch());
            m.put("place",c.getCourse().getPlace());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    public DataResponse courseSave(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
       /* Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();*/
        Integer studentId=s.getStudentId();



        Map form = dataRequest.getMap("form");
        CourseSelection c =null;
        Optional<CourseSelection> oc;

        Integer courseSelectionId;
        courseSelectionId = getNewCourseSelectionId();
        c = new CourseSelection();
        c.setCourseSelectionId(courseSelectionId);


        Integer much = dataRequest.getInteger("much");
        Integer courseId = dataRequest.getInteger("courseId");
        String classTime = dataRequest.getString("classTime");
        Optional<Course> sList=courseRepository.findById(courseId);
        Optional<CourseSelection> sssList=courseSelectionRepository.findByStudentStudentIdAndCourseCourseId(studentId,courseId);
        List<CourseSelection> wrong = courseSelectionRepository.findByStudentStudentIdCourseClassTime(studentId,classTime);
        List<CourseSelection> choose = courseSelectionRepository.findByCourseId(courseId);
        Integer number = choose.size();
        if(number >= much)
            return CommonMethod.getReturnMessageError("课容量已满，无法选择该课程");
        if(sssList.isPresent())
            return CommonMethod.getReturnMessageError("已选择该课，不可重复选课");
        if(!wrong.isEmpty())
            return CommonMethod.getReturnMessageError("上课时间冲突，无法选择该课程");
        c.setCourse(sList.get());
        Optional<Student> ssList=studentRepository.findById(studentId);
        c.setStudent(ssList.get());
        courseSelectionRepository.save(c);
        return CommonMethod.getReturnData(c.getCourseSelectionId());

    }

    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        Optional<CourseSelection> sList=courseSelectionRepository.findByStudentStudentIdAndCourseCourseId(studentId,courseId);
        if(sList.isEmpty())
            return CommonMethod.getReturnMessageError("未选择该课程，无法退选");
        CourseSelection cc;
        cc=sList.get();
          courseSelectionRepository.delete(cc);
        return CommonMethod.getReturnMessageOK();
    }
}
