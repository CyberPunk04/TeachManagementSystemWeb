package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Innovation;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionInnovationService {
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map getMapFromInnovation(Innovation c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("innovationName",c.getInnovationName());
        m.put("innovationTime",c.getInnovationTime());
        m.put("innovationId", c.getInnovationId());
        m.put("innovationContent",c.getInnovationContent());
        m.put("teamName",c.getTeamName());
        m.put("teamNum",c.getTeamNum());
        m.put("teamPosition",c.getTeamPosition());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getSchoolMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Innovation> s=innovationRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromInnovation(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getSchoolMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovationId = dataRequest.getInteger("innovationId");
        Innovation c= null;
        Optional<Innovation> oc;
        if(innovationId != null) {
            oc=innovationRepository.findById(innovationId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromInnovation(c));
    }

    public DataResponse innovationEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovationId = dataRequest.getInteger("innovationId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String innovationName = CommonMethod.getString(form,"innovationName");
        String student = CommonMethod.getString(form,"student");
        Innovation c= null;
        Student s=null;
        Optional<Innovation> oc;
        if(innovationId != null) {
            oc= innovationRepository.findById(innovationId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }
        Optional<Innovation> nOc = innovationRepository.findByInnovationName(innovationName);
        innovationId = c.getInnovationId();
        Optional<Person> nOp = personRepository.findByName(student);

        c.setInnovationName((String)form.get("innovationName"));
        c.setInnovationContent(CommonMethod.getString(form,"innovationContent"));
        c.setInnovationTime(CommonMethod.getString(form,"innovationTime"));
        c.setTeamName(CommonMethod.getString(form,"teamName"));
        c.setTeamNum(CommonMethod.getInteger(form,"teamNum"));
        c.setTeamPosition(CommonMethod.getString(form,"teamPosition"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        innovationRepository.save(c);
        return CommonMethod.getReturnData(c.getInnovationId());
    }

}

