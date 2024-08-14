package org.fatmansoft.teach.service.AdminVersion.Innovation;

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
public class ResearchService {
    @Autowired
    private ResearchRepository researchRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewResearchId(){
        Integer  id = researchRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
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
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getResearchMapList(String numName) {
        List dataList = new ArrayList();
        List<Research> sList =  researchRepository.findResearchListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromResearch(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getResearchMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse researchDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer researchId = dataRequest.getInteger("researchId");
        Research s= null;
        Optional<Research> oc;
        if(researchId != null) {
            oc= researchRepository.findById(researchId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            researchRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
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

    public DataResponse researchEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer researchId = dataRequest.getInteger("researchId");
        Map form = dataRequest.getData(); //参数获取Map对象
        String researchName = CommonMethod.getString(form,"researchName");
        /*String student = CommonMethod.getString(form,"student");
        String num = CommonMethod.getString(form,"num");*/
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Research c= null;
        Student s=null;
        Optional<Research> oc;
        if(researchId != null) {
            oc= researchRepository.findById(researchId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            researchId = getNewResearchId();
            c = new Research();
            c.setResearchId(researchId);
            c.setResearchName(researchName);
        }else {
            researchId = c.getResearchId();
        }

        /*Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/
        c.setResearchName((String)form.get("researchName"));
        c.setResearchContent(CommonMethod.getString(form,"researchContent"));
        c.setResearchTime(CommonMethod.getString(form,"researchTime"));
        /*List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        researchRepository.save(c);
        return CommonMethod.getReturnData(c.getResearchId());
    }
}

