package org.fatmansoft.teach.service.AdminVersion.Innovation;

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
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewLectureId(){
        Integer  id = lectureRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
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
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getLectureMapList(String numName) {
        List dataList = new ArrayList();
        List<Lecture> sList =  lectureRepository.findLectureListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromLecture(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getLectureList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getLectureMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse lectureDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer lectureId = dataRequest.getInteger("lectureId");
        Lecture s= null;
        Optional<Lecture> oc;
        if(lectureId != null) {
            oc= lectureRepository.findById(lectureId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            lectureRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse getLectureInfo(@Valid @RequestBody DataRequest dataRequest) {
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
        Map form = dataRequest.getData(); //参数获取Map对象
        String lectureName = CommonMethod.getString(form,"lectureName");
        /*String student = CommonMethod.getString(form,"student");
        String num = CommonMethod.getString(form,"num");*/
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Lecture c= null;
        Student s=null;
        Optional<Lecture> oc;
        if(lectureId != null) {
            oc= lectureRepository.findById(lectureId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            lectureId = getNewLectureId();
            c = new Lecture();
            c.setLectureId(lectureId);
            c.setLectureName(lectureName);
        }else {
            lectureId = c.getLectureId();
        }

        /*Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/
        c.setLectureName((String)form.get("lectureName"));
        c.setLectureTime(CommonMethod.getString(form,"lectureTime"));
        c.setLecturePosition(CommonMethod.getString(form,"lecturePosition"));
        c.setLectureLeader(CommonMethod.getString(form,"lectureLeader"));
        /*List<Student> sList=studentRepository.findByPersonName(student);
        if(sList!=null&&sList.size()>0){
            c.setStudent(sList.get(0));
        }*/
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        lectureRepository.save(c);
        return CommonMethod.getReturnData(c.getLectureId());
    }
}

