package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")

public class TeacherController {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private TeacherRepository teacherRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private CourseRepository courseRepository;

    public Map getMapFromTeacher(Teacher s) {
        Map m = new HashMap();
        Person p;
        Team t;
        if(s == null)
            return null;
        m.put("title",s.getTitle());
        m.put("degree",s.getDegree());
        m.put("studyDirection",s.getStudyDirection());
        m.put("publications",s.getPublications());
        p = s.getPerson();
        if(p == null)
            return m;
        m.put("teacherId", s.getTeacherId());
        m.put("personId", p.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("dept",p.getDept());
        m.put("card",p.getCard());
        String gender = p.getGender();
        m.put("gender",gender);
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", p.getBirthday());  //时间格式转换字符串
        m.put("email",p.getEmail());
        m.put("phone",p.getPhone());
        m.put("address",p.getAddress());
        m.put("introduce",p.getIntroduce());
        return m;
    }

    public List getTeacherMapList(String numName) {
        List dataList = new ArrayList();
        List<Teacher> sList = teacherRepository.findTeacherListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.size() == 0)
            return dataList;
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(getMapFromTeacher(sList.get(i)));
        }
        return dataList;
    }

    @PostMapping("/getTeacherList")

    public DataResponse getTeacherList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = getTeacherMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }



    @PostMapping("/teacherDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");  //获取student_id值
        Teacher s = null;
        Optional<Teacher> op;
        if (teacherId != null) {
            op = teacherRepository.findById(teacherId);   //查询获得实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        if (s != null) {
            Optional<User> uOp = userRepository.findByPersonPersonId(s.getPerson().getPersonId()); //查询对应该学生的账户
            if (uOp.isPresent()) {
                userRepository.delete(uOp.get()); //删除对应该学生的账户
            }
            Person p = s.getPerson();
            teacherRepository.delete(s);    //首先数据库永久删除学生信息
            personRepository.delete(p);   // 然后数据库永久删除学生信息
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    /**
     * getStudentInfo 前端点击学生列表时前端获取学生详细信息请求服务
     *
     * @param dataRequest 从前端获取 studentId 查询学生信息的主键 student_id
     * @return 根据studentId从数据库中查出数据，存在Map对象里，并返回前端
     */

    @PostMapping("/getTeacherInfo")

    public DataResponse getTeacherInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");
        Integer personId = dataRequest.getInteger("personId");
        Teacher s = null;
        Optional<Teacher> op;
        if (teacherId != null) {
            op = teacherRepository.findById(teacherId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }
        }else if(personId != null){
            op = teacherRepository.findByPersonPersonId(personId);
            if(op.isPresent()){
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromTeacher(s)); //这里回传包含学生信息的Map对象
    }

    @PostMapping("/teacherEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse teacherEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Teacher s = null;
        Person p;
        User u;
        Optional<Teacher> op;
        Integer personId;
        if (teacherId != null) {
            op = teacherRepository.findById(teacherId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if (nOp.isPresent()) {
            if (s == null || !s.getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新学号已经存在，不能添加或修改！");
            }
        }
        if (s == null) {
            p = new Person();
            p.setNum(num);
            p.setType("2");
            personRepository.saveAndFlush(p);  //插入新的Person记录
            String password = encoder.encode("123456");
            u = new User();
            u.setPerson(p);
            u.setUserName(num);
            u.setPassword(password);
            u.setUserType(userTypeRepository.findByName(EUserType.ROLE_TEACHER));
            u.setCreateTime(DateTimeTool.parseDateTime(new Date()));
            u.setCreatorId(CommonMethod.getUserId());
            userRepository.saveAndFlush(u); //插入新的User记录
            s = new Teacher();   // 创建实体对象
            s.setPerson(p);
            teacherRepository.saveAndFlush(s);  //插入新的Student记录
        } else {
            p = s.getPerson();
        }
        personId = p.getPersonId();
        if (!num.equals(p.getNum())) {   //如果人员编号变化，修改人员编号和登录账号
            Optional<User> uOp = userRepository.findByPersonPersonId(personId);
            if (uOp.isPresent()) {
                u = uOp.get();
                u.setUserName(num);
                userRepository.saveAndFlush(u);
            }
            p.setNum(num);  //设置属性
        }
        p.setName(CommonMethod.getString(form, "name"));
        p.setDept(CommonMethod.getString(form, "dept"));
        p.setCard(CommonMethod.getString(form, "card"));
        p.setGender(CommonMethod.getString(form, "gender"));
        p.setBirthday(CommonMethod.getString(form, "birthday"));
        p.setEmail(CommonMethod.getString(form, "email"));
        p.setPhone(CommonMethod.getString(form, "phone"));
        p.setAddress(CommonMethod.getString(form, "address"));
        personRepository.save(p);  // 修改保存人员信息
        s.setTitle(CommonMethod.getString(form, "title"));
        s.setDegree(CommonMethod.getString(form, "degree"));
        s.setStudyDirection(CommonMethod.getString(form,"studyDirection"));
        s.setPublications(CommonMethod.getString(form,"publications"));
        teacherRepository.save(s);  //修改保存学生信息
        return CommonMethod.getReturnData(s.getTeacherId());  // 将studentId返回前端
    }


    public List getTeacherCourseList(Integer teacherId) {
        List<Course> sList=courseRepository.findByTeacherTeacherId(teacherId);
        List list = new ArrayList();
        if (sList == null || sList.size() == 0)
            return list;
        Map m;
        for (Course s : sList) {
            m = new HashMap();
            m.put("courseId", s.getCourseId());
            m.put("num", s.getNum());
            m.put("name", s.getName());
            m.put("credit", s.getCredit());
            m.put("type", s.getType());
            m.put("coursePath", s.getCoursePath());
            //m.put("preCourse", s.getPreCourse());
            m.put("startWeek", s.getStartWeek());
            m.put("endWeek", s.getEndWeek());
            m.put("classTime", s.getClassTime());
            m.put("place", s.getPlace());
            m.put("much", s.getMuch());
            list.add(m);
        }
        return list;
    }
    @PostMapping("/getTeacherIntroduceData")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public DataResponse getTeacherIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        String username = CommonMethod.getUsername();
        Optional<Teacher> sOp = teacherRepository.findByPersonNum(username);  // 查询获得 Teacher对象
        if (!sOp.isPresent())
            return CommonMethod.getReturnMessageError("教师不存在！");
        Teacher s = sOp.get();
        Map info = getMapFromTeacher(s);  // 查询教师信息Map对象
        Map data = new HashMap();
        data.put("info", info);
        data.put("courseList",getTeacherCourseList(s.getTeacherId()));
        return CommonMethod.getReturnData(data);//将前端所需数据保留Map对象里，返还前端
    }

    @PostMapping("/saveTeacherIntroduce")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public DataResponse saveTeacherIntroduce(@Valid @RequestBody DataRequest dataRequest) {
        Integer teacherId = dataRequest.getInteger("teacherId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Teacher s = null;
        Person p;
        Optional<Teacher> op;
        if (teacherId != null) {
            op = teacherRepository.findById(teacherId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        p=s.getPerson();
        s.setStudyDirection(CommonMethod.getString(form,"studyDirection"));
        s.setPublications(CommonMethod.getString(form,"publications"));
        p.setPhone(CommonMethod.getString(form, "phone"));
        p.setAddress(CommonMethod.getString(form, "address"));
        personRepository.save(p);  // 修改保存人员信息
        teacherRepository.save(s);  //修改保存学生信息
        return CommonMethod.getReturnData(s.getTeacherId());  // 将teacherId返回前端
    }

}
