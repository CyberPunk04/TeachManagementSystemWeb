package org.fatmansoft.teach.service.AdminVersion.Innovation;

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
public class InternshipService {
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewInternshipId(){
        Integer  id = internshipRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
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
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getInternshipMapList(String numName) {
        List dataList = new ArrayList();
        List<Internship> sList =  internshipRepository.findInternshipListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromInternship(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getInternshipMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse internshipDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer internshipId = dataRequest.getInteger("internshipId");
        Internship s= null;
        Optional<Internship> oc;
        if(internshipId != null) {
            oc= internshipRepository.findById(internshipId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            internshipRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse getInternshipInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer internshipId = dataRequest.getInteger("internshipId");
        Internship c= null;
        Optional<Internship> oc;
        if(internshipId != null) {
            oc= internshipRepository.findById(internshipId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromInternship(c));
    }

    public DataResponse internshipEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer internshipId = dataRequest.getInteger("internshipId");
        Map form = dataRequest.getData(); //参数获取Map对象
        String company = CommonMethod.getString(form,"company");
        /*String student = CommonMethod.getString(form,"student");
        String num = CommonMethod.getString(form,"num");*/
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Internship c= null;
        Student s=null;
        Optional<Internship> oc;
        if(internshipId != null) {
            oc= internshipRepository.findById(internshipId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }

        if(c == null) {
            internshipId = getNewInternshipId();
            c = new Internship();
            c.setInternshipId(internshipId);
            c.setCompany(company);
        }else {
            internshipId = c.getInternshipId();
        }

        /*Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/

        c.setCompany((String)form.get("company"));
        c.setStartTime(CommonMethod.getString(form,"startTime"));
        c.setInternshipPosition(CommonMethod.getString(form,"internshipPosition"));
        c.setLength(CommonMethod.getString(form,"length"));
        /*List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        internshipRepository.save(c);
        return CommonMethod.getReturnData(c.getInternshipId());
    }
}

