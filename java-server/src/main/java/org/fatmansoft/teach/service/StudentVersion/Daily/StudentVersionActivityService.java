package org.fatmansoft.teach.service.StudentVersion.Daily;

import org.fatmansoft.teach.models.Daily.Activity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    public Map getMapFromActivity(Activity c) {
        Map m = new HashMap();
        Student s;
        Integer studentId=c.getStudentId();
        Optional<Student> student=studentRepository.findById(studentId);
        s=student.get();
        if(c == null)
            return m;
        m.put("activityId", c.getActivityId());
        m.put("studentId",c.getStudentId());
        m.put("studentNum",s.getPerson().getNum());
        m.put("studentName",s.getPerson().getName());
        m.put("activityName",c.getActivityName());
        m.put("activityType",c.getActivityType());
        m.put("activityTime",c.getActivityTime());
        m.put("activityLocation",c.getActivityLocation());
        m.put("studentId",c.getStudentId());
        return m;
    }

    public List getActivityMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Activity> c=activityRepository.findByStudentId(studentId);
        if(c == null || c.size() == 0)
            return dataList;
        for(int i = 0; i < c.size();i++) {
            dataList.add(getMapFromActivity(c.get(i)));
        }
        return dataList;
    }

    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List dataList = getActivityMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

}

