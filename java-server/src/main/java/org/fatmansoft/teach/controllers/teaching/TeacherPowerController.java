package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.teaching.TeacherPower;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.teaching.TeacherPowerRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherPowerController {
    @Autowired
    private TeacherPowerRepository teacherPowerRepository;

    @PostMapping("/teacherPower")
    public DataResponse teacherPowerSave(@Valid @RequestBody DataRequest dataRequest) {
        String value = dataRequest.getString("value");
        TeacherPower t = null;
        int num = 1,power;
        if(value.equals("教师可录入成绩")){
            power = 1;
        }else{
            power = 0;
        }
        Optional<TeacherPower> p;
        p = teacherPowerRepository.findByNum(num);
        t=p.get();
        t.setTeacherPowerId(num);
        t.setPower(power);
        teacherPowerRepository.save(t);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/getPower")
    public DataResponse getPower(@Valid @RequestBody DataRequest dataRequest) {
        TeacherPower t;
        int num = 1;
        Optional<TeacherPower> p;
        p = teacherPowerRepository.findByNum(num);
        t=p.get();
        List dataList = new ArrayList();
        Map m;
            m = new HashMap();
            m.put("power", t.getPower());
            dataList.add(m);
        return CommonMethod.getReturnData(dataList);

    }
}
