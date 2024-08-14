package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Practice;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.PracticeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionPracticeService {
    @Autowired
    private PracticeRepository practiceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map getMapFromPractice(Practice c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("practiceName",c.getPracticeName());
        m.put("practiceTime",c.getPracticeTime());
        m.put("practiceId", c.getPracticeId());
        m.put("practiceContent",c.getPracticeContent());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getPracticeMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Practice> s=practiceRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromPractice(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getPracticeMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer practiceId = dataRequest.getInteger("practiceId");
        Practice c= null;
        Optional<Practice> oc;
        if(practiceId != null) {
            oc= practiceRepository.findById(practiceId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromPractice(c));
    }

    public DataResponse practiceEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer practiceId = dataRequest.getInteger("practiceId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String practiceName = CommonMethod.getString(form,"practiceName");
        String student = CommonMethod.getString(form,"student");
        Practice c= null;
        Student s=null;
        Optional<Practice> oc;
        if(practiceId != null) {
            oc= practiceRepository.findById(practiceId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }
        Optional<Practice> nOc = practiceRepository.findByPracticeName(practiceName);
        practiceId = c.getPracticeId();
        Optional<Person> nOp = personRepository.findByName(student);

        c.setPracticeName((String)form.get("practiceName"));
        c.setPracticeTime(CommonMethod.getString(form,"practiceTime"));
        c.setPracticeContent(CommonMethod.getString(form,"practiceContent"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        practiceRepository.save(c);
        return CommonMethod.getReturnData(c.getPracticeId());
    }

}

