package org.fatmansoft.teach.service.AdminVersion.Innovation;

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
public class InnovationService {
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewInnovationId(){
        Integer  id = innovationRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
    public Map getMapFromInnovation(Innovation c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        String ss=Integer.toString(c.getTeamNum());
        m.put("innovationName",c.getInnovationName());
        m.put("innovationTime",c.getInnovationTime());
        m.put("innovationId", c.getInnovationId());
        m.put("innovationContent",c.getInnovationContent());
        m.put("teamName",c.getTeamName());
        m.put("teamNum",ss);
        m.put("teamPosition",c.getTeamPosition());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getInnovationMapList(String numName) {
        List dataList = new ArrayList();
        List<Innovation> sList =innovationRepository.findInnovationListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromInnovation(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getInnovationMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse innovationDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovationId = dataRequest.getInteger("innovationId");
        Innovation s= null;
        Optional<Innovation> oc;
        if(innovationId != null) {
            oc= innovationRepository.findById(innovationId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            innovationRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse getInnovationInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovationId = dataRequest.getInteger("innovationId");
        Innovation c= null;
        Optional<Innovation> oc;
        if(innovationId != null) {
            oc= innovationRepository.findById(innovationId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromInnovation(c));
    }

    public DataResponse innovationEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovationId = dataRequest.getInteger("innovationId");
        Map form = dataRequest.getData(); //参数获取Map对象
        String innovationName = CommonMethod.getString(form,"innovationName");
        //String student = CommonMethod.getString(form,"student");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Innovation c= null;
        Student s=null;
        Optional<Innovation> oc;
        if(innovationId != null) {
            oc= innovationRepository.findById(innovationId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            innovationId = getNewInnovationId();
            c = new Innovation();
            c.setInnovationId(innovationId);
            c.setInnovationName(innovationName);
        }else {
            innovationId = c.getInnovationId();
        }

       /* Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/

        c.setInnovationName((String)form.get("innovationName"));
        c.setInnovationContent(CommonMethod.getString(form,"innovationContent"));
        c.setInnovationTime(CommonMethod.getString(form,"innovationTime"));
        c.setTeamName(CommonMethod.getString(form,"teamName"));
        c.setTeamNum(CommonMethod.getInteger(form,"teamNum"));
        c.setTeamPosition(CommonMethod.getString(form,"teamPosition"));
        /*List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        innovationRepository.save(c);
        return CommonMethod.getReturnData(c.getInnovationId());
    }
}

