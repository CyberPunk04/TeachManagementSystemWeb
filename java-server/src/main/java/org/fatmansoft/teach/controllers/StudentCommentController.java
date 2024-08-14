package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentCommentRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@CrossOrigin(origins ="*",maxAge = 3600)
@RestController
@RequestMapping("/api/studentComment")
public class StudentCommentController {

    @Autowired
    private StudentCommentRepository studentCommentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    public Map getMapFromStudentComment(StudentComment sc) {
        Map m = new HashMap();

        if(sc == null)
            return null;
        Student commenter = sc.getCommenter();//评论者
        Student commented = sc.getCommented();
        m.put("comment",sc.getComment());
        m.put("studentCommentId",sc.getStudentCommentId());

        Person commenterPerson = commenter.getPerson();
        Person commentedPerson = commented.getPerson();

        if(commenterPerson == null)
            return m;
        if(commentedPerson == null)
            return m;
        m.put("commenter",commenterPerson);
        m.put("commented", commentedPerson);
        m.put("tags",sc.getTags());
        m.put("value",sc.getValue());
        return m;
    }

    //根据被评论者返回所有评论
    @PostMapping("/getCommentList")
    public DataResponse getCommentList(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");

        //如果你想用当前登录用户的id，可以用下面的代码，前端就不用传参数了
        if(studentId == null) {
            Integer userId = CommonMethod.getUserId();
            if (userId == null)
                return CommonMethod.getReturnMessageError("请先登录");
            Optional<User> user = userRepository.findById(userId);
            Integer personId = null;
            if (user.isPresent()) {
                personId = user.get().getPerson().getPersonId();
            }
            Optional<Student> student = studentRepository.findById(personId);
            if (student.isPresent()) {
                studentId = student.get().getStudentId();
            }

        }


        List dataList = new ArrayList();
        List<StudentComment> cList = studentCommentRepository.findByCommentedId(studentId);  //数据库查询操作
        if (cList == null || cList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < cList.size(); i++) {
            dataList.add(getMapFromStudentComment(cList.get(i)));
        }
        return CommonMethod.getReturnData(dataList);
    }

    /**
     * 获取当前用户的平均分
     *
     * @param dataRequest
     * @return
     */
    @PostMapping("/getAverageValue")
    public DataResponse getAverageValue(@Valid@RequestBody DataRequest dataRequest){
       // Integer studentId = dataRequest.getInteger("studentId");

        Integer userId = CommonMethod.getUserId();
        if(userId == null)
            return CommonMethod.getReturnMessageError("请先登录");
        Optional<User> user = userRepository.findById(userId);
        Integer personId = user.get().getPerson().getPersonId();
        Optional<Student> student = studentRepository.findByPersonPersonId(personId);

        Integer studentId = student.get().getStudentId();

        List<StudentComment> scList = studentCommentRepository.findByCommentedId(studentId);
        if (scList == null || scList.size() == 0)
            return CommonMethod.getReturnMessageError("没有评价");
        double sum = 0.0;
        for (int i = 0; i < scList.size(); i++) {
            sum += scList.get(i).getValue();
        }
        //保留两位小数
        double average = (double) Math.round(sum / scList.size() * 100) / 100;
        return CommonMethod.getReturnData(average);
    }


    //给某个学生写评价
    @PostMapping("/addComment")
    public DataResponse addComment(@Valid@RequestBody DataRequest dataRequest){
        Integer commentedId = dataRequest.getInteger("commentedId");
        String  comment = dataRequest.getString("comment");
        String  tags    = dataRequest.getString("tags");
        Integer value =dataRequest.getInteger("value");

        //获取评价者
        Integer userId = CommonMethod.getUserId();
        if(userId == null)
            return CommonMethod.getReturnMessageError("请先登录");
        Optional<User> user = userRepository.findById(userId);
        Integer personId = null;
        if(user.isPresent()){
            personId = user.get().getPerson().getPersonId();
        }
        Optional<Student> student = studentRepository.findByPersonPersonId(personId);
        Student commenter = null;
        if(student.isPresent()){
            commenter = student.get();
        }
        StudentComment sc = new StudentComment();
        sc.setCommenter(commenter);

        //获取被评价者
        Optional<Student> commented = studentRepository.findById(commentedId);

        Optional<StudentComment> sc1 = studentCommentRepository.findByCommenterIdCommentedId(commenter.getStudentId(),commentedId);
        if(sc1.isPresent()){
            return CommonMethod.getReturnMessageError("已经评价过该学生");
        }

        if(commented.isPresent())
            sc.setCommented(commented.get());

        sc.setComment(comment);
        sc.setTags(tags);
        sc.setValue(value);
        studentCommentRepository.saveAndFlush(sc);
        return CommonMethod.getReturnMessageOK();
    }

    //删除某个学生的评价
    @PostMapping("/deleteComment")
    public DataResponse deleteComment(@Valid@RequestBody DataRequest dataRequest){
        Integer studentCommentId = dataRequest.getInteger("studentCommentId");
        studentCommentRepository.deleteById(studentCommentId);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getAllComment")
    public DataResponse getAllComment(@Valid@RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        List<StudentComment> cList = studentCommentRepository.findAll();  //数据库查询操作
        if (cList == null || cList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < cList.size(); i++) {
            dataList.add(getMapFromStudentComment(cList.get(i)));
        }
        return CommonMethod.getReturnData(dataList);
    }

    //根据评论者学号和名字查询其发送的评价
    @PostMapping("/searchComment")
    public DataResponse searchComment(@Valid@RequestBody DataRequest dataRequest){
        String numName = dataRequest.getString("numName");
        List<StudentComment> scList = studentCommentRepository.findByNumName(numName);
        List dataList = new ArrayList();
        if (scList == null || scList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        for (int i = 0; i < scList.size(); i++) {
            dataList.add(getMapFromStudentComment(scList.get(i)));
        }
        return CommonMethod.getReturnData(dataList);
    }
}
