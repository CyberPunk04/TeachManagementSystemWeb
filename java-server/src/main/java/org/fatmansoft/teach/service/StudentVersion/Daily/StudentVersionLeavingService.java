package org.fatmansoft.teach.service.StudentVersion.Daily;

import org.fatmansoft.teach.models.Daily.Leaving;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.LeavingRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionLeavingService {
    @Autowired
    private LeavingRepository leavingRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    public synchronized Integer getNewLeavingId() {
        Integer id = leavingRepository.getMaxId();  // 查询最大的id
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }

    public Map getSmallMapFromLeaving(Leaving l) {
        Map m = new HashMap();
        Student s;
        if(l == null)
            return m;
        m.put("leavingId", l.getLeavingId());
        m.put("campus",l.getCampus());
        m.put("destination",l.getDestination());
        m.put("reason",l.getReason());
        m.put("leavingTime",l.getLeavingTime());
        m.put("startTime",l.getStartTime());
        m.put("endTime",l.getEndTime());
        m.put("approval",l.getApproval());
        m.put("approvalCondition", ComDataUtil.getInstance().getDictionaryLabelByValue("SPZM", l.getApproval()));
        return m;
    }

    public Map getMapFromLeaving(Leaving l) {
        Map m = new HashMap();
        Student s;
        if(l == null)
            return m;
        m.put("leavingId", l.getLeavingId());
        m.put("studentId",l.getStudent().getStudentId());
        m.put("studentNum",l.getStudent().getPerson().getNum());
        m.put("studentName",l.getStudent().getPerson().getName());
        m.put("className",l.getStudent().getClassName());
        m.put("campus",l.getCampus());
        m.put("destination",l.getDestination());
        m.put("reason",l.getReason());
        m.put("leavingTime",l.getLeavingTime());
        m.put("startTime",l.getStartTime());
        m.put("endTime",l.getEndTime());
        m.put("approval",l.getApproval());
        m.put("approvalCondition", ComDataUtil.getInstance().getDictionaryLabelByValue("SPZM", l.getApproval()));
        s=l.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getLeavingMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Leaving> sList = leavingRepository.findByStudentStudentId(studentId);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromLeaving(sList.get(i)));
        }
        return dataList;
    }

    public List getLeavingMapListByNumName(String numName) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List dataList = new ArrayList();
        List<Leaving> sList =  leavingRepository.findLeavingListByNumNameAndStudentId(numName,studentId);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromLeaving(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getLeavingList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List dataList = getLeavingMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getLeavingListByNumName(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getLeavingMapListByNumName(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse leavingDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer leavingId = dataRequest.getInteger("leavingId");
        Leaving s= null;
        Optional<Leaving> oc;
        if(leavingId != null) {
            oc= leavingRepository.findById(leavingId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            leavingRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse getLeavingInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer leavingId = dataRequest.getInteger("leavingId");
        Leaving c= null;
        Optional<Leaving> oc;
        if(leavingId != null) {
            oc= leavingRepository.findById(leavingId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getSmallMapFromLeaving(c));
    }

    public DataResponse leavingEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();


        Integer leavingId = dataRequest.getInteger("leavingId");
        Map form = dataRequest.getMap("form");
        Leaving c= null;
        Optional<Leaving> oc;
        if(leavingId != null) {
            oc= leavingRepository.findById(leavingId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }

        if(c == null) {
            leavingId = getNewLeavingId();
            c = new Leaving();
            c.setLeavingId(leavingId);
        }else {
            leavingId = c.getLeavingId();
        }

        c.setCampus(CommonMethod.getString(form,"campus"));
        c.setDestination(CommonMethod.getString(form,"destination"));
        c.setReason(CommonMethod.getString(form,"reason"));
        c.setLeavingTime(CommonMethod.getString(form,"leavingTime"));
        c.setStartTime(CommonMethod.getString(form,"startTime"));
        c.setEndTime(CommonMethod.getString(form,"endTime"));
        Optional<Student> sList=studentRepository.findById(studentId);
        c.setStudent(sList.get());


        leavingRepository.save(c);
        return CommonMethod.getReturnData(c.getLeavingId());
    }

}
