package org.fatmansoft.teach.service.StudentVersion.Prize;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Prize.Prize;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.PrizeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionPrizeService {
    @Autowired
    private PrizeRepository prizeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
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
        m.put("prizeName",c.getPrizeName());
        m.put("gotTime",c.getGotTime());
        m.put("prizeId", c.getPrizeId());
        m.put("prizeLevel",c.getPrizeLevel());
        m.put("prizeType",c.getPrizeType());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getPrizeMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Prize> c=prizeRepository.findByStudentStudentId(studentId);
        if(c == null || c.size() == 0)
            return dataList;
        for(int i = 0; i < c.size();i++) {
            dataList.add(getMapFromPrize(c.get(i)));
        }
        return dataList;
    }

    public DataResponse getPrizeList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List dataList = getPrizeMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

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

    public DataResponse prizeEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer prizeId = dataRequest.getInteger("prizeId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String prizeName = CommonMethod.getString(form,"prizeName");
        String student = CommonMethod.getString(form,"student");
        String num = CommonMethod.getString(form,"num");
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

        Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }
        c.setPrizeName((String)form.get("prizeName"));
        c.setPrizeLevel(CommonMethod.getString(form,"prizeLevel"));
        c.setGotTime(CommonMethod.getString(form,"gotTime"));
        c.setPrizeType(CommonMethod.getString(form,"prizeType"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        prizeRepository.save(c);
        return CommonMethod.getReturnData(c.getPrizeId());
    }

}

