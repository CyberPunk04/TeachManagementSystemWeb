package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Research;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.ResearchRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionResearchService {
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map getMapFromResearch(Research c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("researchName",c.getResearchName());
        m.put("researchTime",c.getResearchTime());
        m.put("researchId", c.getResearchId());
        m.put("researchContent",c.getResearchContent());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getResearchMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Research> s=researchRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromResearch(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getResearchMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer researchId = dataRequest.getInteger("researchId");
        Research c= null;
        Optional<Research> oc;
        if(researchId != null) {
            oc= researchRepository.findById(researchId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromResearch(c));
    }

    public DataResponse schoolEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer researchId = dataRequest.getInteger("researchId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String researchName = CommonMethod.getString(form,"researchName");
        String student = CommonMethod.getString(form,"student");
        Research c= null;
        Student s=null;
        Optional<Research> oc;
        if(researchId != null) {
            oc= researchRepository.findById(researchId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }
        Optional<Research> nOc = researchRepository.findByResearchName(researchName);
        researchId = c.getResearchId();
        Optional<Person> nOp = personRepository.findByName(student);

        c.setResearchName((String)form.get("researchName"));
        c.setResearchContent(CommonMethod.getString(form,"researchContent"));
        c.setResearchTime(CommonMethod.getString(form,"researchTime"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        researchRepository.save(c);
        return CommonMethod.getReturnData(c.getResearchId());
    }

}

