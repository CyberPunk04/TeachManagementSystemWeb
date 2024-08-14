package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.StudentService;
import org.fatmansoft.teach.service.TeacherService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    // 查询是否已关注的方法
    @PostMapping("/getFollow")
    public DataResponse getFollow(@Valid@RequestBody DataRequest dataRequest) {
        Integer followingId = dataRequest.getInteger("followingId");
        Integer followerId = null;
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        if(op.isPresent()){
            User user = op.get();
            followerId = user.getPerson().getPersonId();
        }

        Follow existingFollow = followRepository.findByFollowerAndFollowing(followerId, followingId);
        if (existingFollow == null) {
            return CommonMethod.getReturnMessageError("未关注");
        } else {
            return CommonMethod.getReturnMessageOK();
        }
    }

    // 关注或取消关注的方法
    @PostMapping("/updateFollow")
    public DataResponse toggleFollow(@Valid@RequestBody DataRequest dataRequest) {
        Integer followingID = dataRequest.getInteger("followingId");
        Integer followerID = null;
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        if(op.isPresent()){
            User user = op.get();
            followerID = user.getPerson().getPersonId();
        }

        Follow existingFollow = followRepository.findByFollowerAndFollowing(followerID, followingID);

        if (existingFollow == null) {
            // 如果未关注，则关注
            Follow newFollow = new Follow();
            Optional<Person> op1 = personRepository.findById(followerID);
            Optional<Person> op2 = personRepository.findById(followingID);
            Person follower = new Person();
            Person following = new Person();
            if(op1.isPresent() && op2.isPresent()) {
                follower = op1.get();
                following = op2.get();
            }

            newFollow.setFollower(follower);
            newFollow.setFollowing(following);
            followRepository.save(newFollow);
            return CommonMethod.getReturnMessageOK();
        } else {
            // 如果已关注，则取消关注
            followRepository.delete(existingFollow);
            return CommonMethod.getReturnMessageOK("取消关注成功");
        }
    }

    //获取好友列表的方法
    @PostMapping("/getFriendList")
    public DataResponse getFriendList(@Valid@RequestBody DataRequest dataRequest){
        String numName = dataRequest.getString("numName");
        Integer userId = CommonMethod.getUserId();
        Optional<User> op = Optional.empty();
        if (userId != null) {
            op = userRepository.findById(userId);
        }
        Person person = new Person();
        if (op.isPresent()) {
            User user = op.get();
            person = user.getPerson();
        }
        Integer personId = person.getPersonId();
        List<Person> friendList = followRepository.findMutualFollowing(personId);
        //转变成studentlist或者teacherlist
        List dataList = new ArrayList();
        if(numName!= null) {
            for (Person p : friendList) {
                if ("1".equals(p.getType())) {
                    List<Student> sList = studentRepository.findStudentListByNumName(numName);
                    for (Student s : sList) {
                        if (s.getPerson().getPersonId().equals(p.getPersonId())) {
                            dataList.add(studentService.getMapFromStudent(s));
                        }
                    }
                } else if ("2".equals(p.getType())) {
                    List<Teacher> tList = teacherRepository.findTeacherListByNumName(numName);
                    for (Teacher t : tList) {
                        if (t.getPerson().getPersonId().equals(p.getPersonId())) {
                            dataList.add(teacherService.getMapFromTeacher(t));
                        }
                    }
                    // dataList.add(teacherService.getMapFromTeacher(teacherRepository.findByPersonPersonId(p.getPersonId()).get()));
                }
            }
        }else {
            for (Person p : friendList) {
                if ("1".equals(p.getType())) {
                    dataList.add(studentService.getMapFromStudent(studentRepository.findByPersonPersonId(p.getPersonId()).get()));
                } else if ("2".equals(p.getType())) {
                    dataList.add(teacherService.getMapFromTeacher(teacherRepository.findByPersonPersonId(p.getPersonId()).get()));
                }
            }
        }
        return CommonMethod.getReturnData(dataList);
    }


    // 统计粉丝数量的方法
    @PostMapping("/getFansNum")
    public DataResponse countFollowers(@Valid@RequestBody DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");

        int fansNum = followRepository.countByFollowing(personId);

        return CommonMethod.getReturnData(fansNum);
    }

}
