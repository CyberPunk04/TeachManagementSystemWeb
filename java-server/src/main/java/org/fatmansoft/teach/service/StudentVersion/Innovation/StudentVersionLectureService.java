package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Lecture;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.LectureRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class StudentVersionLectureService {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;

    public Map getMapFromLecture(Lecture c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("lectureName",c.getLectureName());
        m.put("lectureTime",c.getLectureTime());
        m.put("lectureId", c.getLectureId());
        m.put("lectureLeader",c.getLectureLeader());
        m.put("lecturePosition",c.getLecturePosition());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        return m;
    }

    public List getLectureMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Lecture> s=lectureRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromLecture(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getLectureMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer lectureId = dataRequest.getInteger("lectureId");
        Lecture c= null;
        Optional<Lecture> oc;
        if(lectureId != null) {
            oc= lectureRepository.findById(lectureId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromLecture(c));
    }

    public DataResponse lectureEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer lectureId = dataRequest.getInteger("lectureId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String lectureName = CommonMethod.getString(form,"lectureschoolName");
        String student = CommonMethod.getString(form,"student");
        Lecture c= null;
        Student s=null;
        Optional<Lecture> oc;
        if(lectureId != null) {
            oc= lectureRepository.findById(lectureId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }
        Optional<Lecture> nOc = lectureRepository.findByLectureName(lectureName);
        lectureId = c.getLectureId();
        Optional<Person> nOp = personRepository.findByName(student);

        c.setLectureName((String)form.get("lectureName"));
        c.setLectureTime(CommonMethod.getString(form,"lectureTime"));
        c.setLecturePosition(CommonMethod.getString(form,"lecturePosition"));
        c.setLectureLeader(CommonMethod.getString(form,"lectureLeader"));
        List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }

        lectureRepository.save(c);
        return CommonMethod.getReturnData(c.getLectureId());
    }

}

