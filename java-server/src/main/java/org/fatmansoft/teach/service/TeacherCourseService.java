package org.fatmansoft.teach.service.Teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

@Service

public class TeacherCourseService {
    @Value("${attach.folder}")    //环境配置变量获取
    private String attachFolder;  //服务器端数据存储
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    public Map getMapFromCourse(Course c) {
        Map m = new HashMap();
        Teacher t;
        Course s;
        if(c == null)
            return m;
        m.put("name",c.getName());
        m.put("num",c.getNum());
        m.put("courseId", c.getCourseId());
        m.put("credit",c.getCredit());
        s=c.getPreCourse();
        m.put("preCourseId",s.getCourseId());
        m.put("preCourse",s.getName());
        m.put("place",c.getPlace());
        m.put("classTime",c.getClassTime());
        m.put("startWeek",c.getStartWeek());
        m.put("endWeek",c.getEndWeek());
        m.put("type",c.getType());
        m.put("much",c.getMuch());
        m.put("coursePath",c.getCoursePath());
        t=c.getTeacher();
        if(t==null){
            return m;
        }
        m.put("teacher",t.getPerson().getName());
        return m;
    }

    public List getCourseMapList(Integer teacherId,String numName) {
        List dataList = new ArrayList();
        List<Course> cList =  courseRepository.findByTeacherTeacherIdNumName(teacherId,numName);
        for(int i = 0; i < cList.size();i++) {
            dataList.add(getMapFromCourse(cList.get(i)));
        }//数据库查询操作
        return dataList;
    }

    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        Teacher t=teacherRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer teacherId=t.getTeacherId();
        String numName = dataRequest.getString("numName");
        if(numName == null)
            numName = "";
        /*String userName = dataRequest.getString("numName");
        Optional<User> tt;
        Optional<Teacher> te;
        tt = userRepository.findByUserName(userName);
        Integer personId = tt.get().getPerson().getPersonId();
        te = teacherRepository.findByPersonPersonId(personId);
        Integer teacherId = te.get().getTeacherId();*/
        List dataList = getCourseMapList(teacherId,numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    public DataResponse getCourseInfo(@Valid @RequestBody DataRequest dataRequest) {
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

    public DataResponse uploadPhoto(@RequestBody byte[] barr,
                                    @RequestParam(name = "uploader") String uploader,
                                    @RequestParam(name = "remoteFile") String remoteFile,
                                    @RequestParam(name = "fileName") String fileName)  {
        try {
            OutputStream os = new FileOutputStream(new File(attachFolder + remoteFile));
            os.write(barr);
            os.close();
            return CommonMethod.getReturnMessageOK();
        }catch(Exception e){
            return CommonMethod.getReturnMessageError("上传错误");
        }
    }

}
