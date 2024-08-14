package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.teaching.CourseSelectionRepository;
import org.fatmansoft.teach.service.Teaching.CourseSelectionService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentCourse")
public class StudentScoreController {
    @Autowired
    private CourseSelectionRepository courseSelectionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/getCourseItemOptionList")
    public OptionItemList getCourseItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();
        List<CourseSelection> sList = courseSelectionRepository.findByStudentId(studentId);  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (CourseSelection c : sList) {
            itemList.add(new OptionItem(c.getCourse().getCourseId(),c.getCourse().getCourseId()+"", c.getCourse().getNum()+"-"+c.getCourse().getName()));
        }
        return new OptionItemList(0, itemList);
    }
}
