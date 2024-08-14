package org.fatmansoft.teach.service.AdminVersion.Prize;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Prize.Prize;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.PrizeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class PrizeService {
    @Autowired
    private PrizeRepository prizeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewPrizeId(){
        Integer  id = prizeRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
    public Map getMapFromPrize(Prize c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("prizeId", c.getPrizeId());
        m.put("prizeName",c.getPrizeName());
        m.put("gotTime",c.getGotTime());
        m.put("prizeType",c.getPrizeType());
        m.put("prizeLevel",c.getPrizeLevel());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getPrizeMapList(String numName) {
        List dataList = new ArrayList();
        List<Prize> sList = prizeRepository.findPrizeListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromPrize(sList.get(i)));
        }
        return dataList;
    }
    @PostMapping("/getPrizeList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getPrizeMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }



    @PostMapping("/prizeDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse prizeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer prizeId = dataRequest.getInteger("prizeId");
        Prize s= null;
        Optional<Prize> oc;
        if(prizeId != null) {
            oc= prizeRepository.findById(prizeId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            prizeRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/getPrizeInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getPrizeInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer prizeId = dataRequest.getInteger("prizeId");
        Prize c= null;
        Optional<Prize> oc;
        if(prizeId != null) {
            oc= prizeRepository.findById(prizeId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromPrize(c));
    }

    @PostMapping("/prizeEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse prizeEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer prizeId = dataRequest.getInteger("prizeId");
        Map form = dataRequest.getData(); //参数获取Map对象
        String prizeName = CommonMethod.getString(form,"prizeName");
        //String student = CommonMethod.getString(form,"student");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Prize c= null;
        Student s=null;
        Optional<Prize> oc;
        if(prizeId != null) {
            oc= prizeRepository.findById(prizeId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            prizeId = getNewPrizeId();
            c = new Prize();
            c.setPrizeId(prizeId);
            c.setPrizeName(prizeName);
        }else {
            prizeId = c.getPrizeId();
        }

       /* Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/
        c.setPrizeName((String)form.get("prizeName"));
        c.setPrizeLevel(CommonMethod.getString(form,"prizeLevel"));
        c.setGotTime(CommonMethod.getString(form,"gotTime"));
        c.setPrizeType(CommonMethod.getString(form,"prizeType"));
       /* List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> student=studentRepository.findById(studentId);
        if(student.isPresent()){
            c.setStudent(student.get());
        }

        prizeRepository.saveAndFlush(c);
        return CommonMethod.getReturnData(c.getPrizeId());
    }
}

