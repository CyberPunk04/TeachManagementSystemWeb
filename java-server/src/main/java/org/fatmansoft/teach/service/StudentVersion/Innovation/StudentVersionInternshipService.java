package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Internship;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.InternshipRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionInternshipService {
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map getMapFromInternship(Internship c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("internshipId",c.getInternshipId());
        m.put("company",c.getCompany());
        m.put("startTime",c.getStartTime());
        m.put("internshipPosition", c.getInternshipPosition());
        m.put("length",c.getLength());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getIntershipMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Internship> s=internshipRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromInternship(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getInternshipList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getIntershipMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer internshipId = dataRequest.getInteger("internshipId");
        Internship c= null;
        Optional<Internship> oc;
        if(internshipId != null) {
            oc=internshipRepository.findById(internshipId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromInternship(c));
    }

    public DataResponse internshipEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer internshipId = dataRequest.getInteger("internshipId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String company = CommonMethod.getString(form,"company");
        String student = CommonMethod.getString(form,"student");
        Internship c= null;
        Student s=null;
        Optional<Internship> oc;
        if(internshipId != null) {
            oc= internshipRepository.findById(internshipId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }
        Optional<Internship> nOc = internshipRepository.findByCompany(company);
        internshipId = c.getInternshipId();
        Optional<Person> nOp = personRepository.findByName(student);

        c.setCompany((String)form.get("company"));
        c.setStartTime(CommonMethod.getString(form,"startTime"));
        c.setInternshipPosition(CommonMethod.getString(form,"internshipPosition"));
        c.setLength(CommonMethod.getString(form,"length"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        internshipRepository.save(c);
        return CommonMethod.getReturnData(c.getInternshipId());
    }

}

