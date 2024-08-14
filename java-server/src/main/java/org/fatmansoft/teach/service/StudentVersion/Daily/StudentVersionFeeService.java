package org.fatmansoft.teach.service.StudentVersion.Daily;

import org.fatmansoft.teach.models.Daily.Fee;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.FeeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionFeeService {
    @Autowired
    private FeeRepository FeeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    public Map getMapFromFee(Fee f) {
        Map m = new HashMap();
        Student s;
        if(f == null)
            return m;
        m.put("feeId", f.getFeeId());
        m.put("studentId",f.getStudent().getStudentId());
        m.put("studentNum",f.getStudent().getPerson().getNum());
        m.put("studentName",f.getStudent().getPerson().getName());
        m.put("className",f.getStudent().getClassName());
        m.put("day",f.getDay());
        m.put("money",f.getMoney());
        s=f.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getFeeMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Fee> c=FeeRepository.findByStudentStudentId(studentId);
        if(c == null || c.size() == 0)
            return dataList;
        for(int i = 0; i < c.size();i++) {
            dataList.add(getMapFromFee(c.get(i)));
        }
        return dataList;
    }

    public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List dataList = getFeeMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

}

