package org.fatmansoft.teach.service.AdminVersion.Innovation;

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
public class PracticeService {
    @Autowired
    private PracticeRepository practiceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewPracticeId(){
        Integer  id = practiceRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
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
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getPracticeMapList(String numName) {
        List dataList = new ArrayList();
        List<Practice> sList =  practiceRepository.findPracticeListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromPractice(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getPracticeMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }


    public DataResponse practiceDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer practiceId = dataRequest.getInteger("practiceId");
        Practice s= null;
        Optional<Practice> oc;
        if(practiceId != null) {
            oc= practiceRepository.findById(practiceId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            practiceRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
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
        Map form = dataRequest.getData(); //参数获取Map对象
        String practiceName = CommonMethod.getString(form,"practiceName");
       /* String student = CommonMethod.getString(form,"student");
        String num = CommonMethod.getString(form,"num");*/
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Practice c= null;
        Student s=null;
        Optional<Practice> oc;
        if(practiceId != null) {
            oc= practiceRepository.findById(practiceId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            practiceId = getNewPracticeId();
            c = new Practice();
            c.setPracticeId(practiceId);
            c.setPracticeName(practiceName);
        }else {
            practiceId = c.getPracticeId();
        }

        /*Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/
        c.setPracticeName((String)form.get("practiceName"));
        c.setPracticeContent(CommonMethod.getString(form,"practiceContent"));
        c.setPracticeTime(CommonMethod.getString(form,"practiceTime"));
        /*List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        practiceRepository.save(c);
        return CommonMethod.getReturnData(c.getPracticeId());
    }
}
