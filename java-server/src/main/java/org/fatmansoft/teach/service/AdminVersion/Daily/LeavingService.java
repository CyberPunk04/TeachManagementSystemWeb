package org.fatmansoft.teach.service.AdminVersion.Daily;

import org.fatmansoft.teach.models.Daily.Leaving;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.LeavingRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class LeavingService {
    @Autowired
    private LeavingRepository leavingRepository;
    @Autowired
    private StudentRepository studentRepository;

    public synchronized Integer getNewLeavingId(){
        Integer  id = leavingRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }

    public Map getMapFromLeaving(Leaving l) {
        Map m = new HashMap();
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
        return m;
    }

    public List getLeavingMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Leaving> lList = leavingRepository.findListByStudent(studentId);
        if(lList == null || lList.size() == 0)
            return dataList;
        for(int i = 0; i < lList.size();i++) {
            dataList.add(getMapFromLeaving(lList.get(i)));
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

    public DataResponse getLeavingList(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId =0;
        List dataList =getLeavingMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse leavingDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer leavingId = dataRequest.getInteger("leavingId");
        Optional<Leaving> op;
        Leaving l = null;
        if(leavingId != null) {
            op= leavingRepository.findByLeavingId(leavingId);
            if(op.isPresent()) {
                l = op.get();
                leavingRepository.delete(l);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse leavingSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Integer leavingId = dataRequest.getInteger("leavingId");
        String campus = dataRequest.getString("campus");
        String destination = dataRequest.getString("destination");
        String reason = dataRequest.getString("reason");
        String leavingTime = dataRequest.getString("leavingTime");
        String startTime = dataRequest.getString("startTime");
        String endTime = dataRequest.getString("endTime");
        String approval = dataRequest.getString("approval");
        Optional<Leaving> op;
        Leaving l = null;
        if(leavingId != null) {
            op= leavingRepository.findByLeavingId(leavingId);
            if(op.isPresent())
                l = op.get();
        }
        if(l == null) {
            l = new Leaving();
            l.setLeavingId(getNewLeavingId());
            l.setStudent(studentRepository.findById(studentId).get());
        }
        l.setCampus(campus);
        l.setDestination(destination);
        l.setReason(reason);
        l.setLeavingTime(leavingTime);
        l.setStartTime(startTime);
        l.setEndTime(endTime);
        l.setApproval(approval);
        leavingRepository.saveAndFlush(l);
        return CommonMethod.getReturnMessageOK();
    }

}

