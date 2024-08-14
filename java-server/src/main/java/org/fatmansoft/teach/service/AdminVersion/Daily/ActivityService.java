package org.fatmansoft.teach.service.AdminVersion.Daily;

import org.fatmansoft.teach.models.Daily.Activity;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private StudentRepository studentRepository;

    /**
     *  获取 activity 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 activity_id
     * @return
     */
    public synchronized Integer getNewActivityId(){
        Integer  id = activityRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }

    /**
     *  getActivityMapList 根据输入参数查询得到请假数据的 Map List集合 参数为空，查出所有活动；参数不为空，查出应studentId的学生的活动
     * @param studentId 输入参数
     * @return  Map List 集合
     */
    public List getActivityMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Activity> aList = activityRepository.findListByStudent(studentId);
        if(aList == null || aList.size() == 0)
            return dataList;
        for(int i = 0; i < aList.size();i++) {
            dataList.add(getMapFromActivity(aList.get(i)));
        }
        return dataList;
    }

    /**
     * getMapFromActivity 将请假表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromActivity(Activity a) {
        Student s;
        Integer studentId=a.getStudentId();
        Optional<Student> student=studentRepository.findById(studentId);
        s=student.get();
        Map m = new HashMap();
        if(a == null)
            return m;
        m.put("activityId", a.getActivityId());
        m.put("studentId",a.getStudentId());
        m.put("studentNum",s.getPerson().getNum());
        m.put("studentName",s.getPerson().getName());
        m.put("activityName",a.getActivityName());
        m.put("activityType",a.getActivityType());
        m.put("activityTime",a.getActivityTime());
        m.put("activityLocation",a.getActivityLocation());
        return m;
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

    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId =0;
        List dataList = getActivityMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer activityId = dataRequest.getInteger("activityId");
        Optional<Activity> op;
        Activity a = null;
        if(activityId != null) {
            op= activityRepository.findByActivityId(activityId);
            if(op.isPresent()) {
                a = op.get();
                activityRepository.delete(a);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse activitySave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Integer activityId = dataRequest.getInteger("activityId");
        String activityName = dataRequest.getString("activityName");
        String activityType = dataRequest.getString("activityType");
        String activityTime = dataRequest.getString("activityTime");
        String activityLocation = dataRequest.getString("activityLocation");
        Optional<Activity> op;
        Activity a = null;
        if(activityId != null) {
            op= activityRepository.findByActivityId(activityId);
            if(op.isPresent())
                a = op.get();
        }
        if(a == null) {
            a = new Activity();
            a.setActivityId(getNewActivityId());
        }
        a.setStudentId(studentId);
        a.setActivityName(activityName);
        a.setActivityType(activityType);
        a.setActivityTime(activityTime);
        a.setActivityLocation(activityLocation);
        activityRepository.saveAndFlush(a);
        return CommonMethod.getReturnMessageOK();
    }
}