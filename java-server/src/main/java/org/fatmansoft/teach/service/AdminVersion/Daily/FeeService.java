package org.fatmansoft.teach.service.AdminVersion.Daily;

import org.fatmansoft.teach.models.Daily.Fee;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.FeeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class FeeService {
    @Autowired
    private FeeRepository feeRepository;
    @Autowired
    private StudentRepository studentRepository;

    public synchronized Integer getNewFeeId(){
        Integer  id = feeRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }

    public Map getMapFromFee(Fee f) {
        Student s=f.getStudent();
        Map m = new HashMap();
        if(f == null)
            return m;
        m.put("feeId", f.getFeeId());
        m.put("studentId",f.getStudent().getStudentId());
        m.put("studentNum",f.getStudent().getPerson().getNum());
        m.put("studentName",f.getStudent().getPerson().getName());
        m.put("className",f.getStudent().getClassName());
        m.put("day",f.getDay());
        m.put("money",f.getMoney());
        return m;
    }

    public List getFeeMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Fee> fList = feeRepository.findListByStudent(studentId);
        if(fList == null || fList.size() == 0)
            return dataList;
        for(int i = 0; i < fList.size();i++) {
            dataList.add(getMapFromFee(fList.get(i)));
        }
        return dataList;
    }

    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem(s.getStudentId(), s.getPerson().getNum(), s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }

    public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId =0;
        List dataList = getFeeMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse feeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer feeId = dataRequest.getInteger("feeId");
        Optional<Fee> op;
        Fee f = null;
        if(feeId != null) {
            op= feeRepository.findByFeeId(feeId);
            if(op.isPresent()) {
                f = op.get();
                feeRepository.delete(f);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse feeSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Integer feeId = dataRequest.getInteger("feeId");
        String day = dataRequest.getString("day");
        Double money = dataRequest.getDouble("money");
        Optional<Fee> op;
        Fee f = null;
        if(feeId != null) {
            op= feeRepository.findByFeeId(feeId);
            if(op.isPresent())
                f = op.get();
        }
        if(f == null) {
            f = new Fee();
            f.setFeeId(getNewFeeId());
        }
        f.setStudent(studentRepository.findById(studentId).get());
        f.setDay(day);
        f.setMoney(money);
        feeRepository.saveAndFlush(f);
        return CommonMethod.getReturnMessageOK();
    }
}

