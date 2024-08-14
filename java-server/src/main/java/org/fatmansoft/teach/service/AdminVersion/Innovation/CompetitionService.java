package org.fatmansoft.teach.service.AdminVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Competition;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CompetitionRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.*;

@Service
public class CompetitionService {
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    public synchronized Integer getNewCompetitionId(){
        Integer  id = competitionRepository.getMaxId();
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }
    public Map getMapFromCompetition(Competition c) {
        Map m = new HashMap();
        Student s;
        if(c == null)
            return m;
        m.put("competitionName",c.getCompetitionName());
        m.put("competitionTime",c.getCompetitionTime());
        m.put("competitionId", c.getCompetitionId());
        m.put("competitionLevel",c.getCompetitionLevel());
        m.put("competitionPrize",c.getCompetitionPrize());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        m.put("num",s.getPerson().getNum());
        return m;
    }
    public List getCompetitionMapList(String numName) {
        List dataList = new ArrayList();
        List<Competition> sList =  competitionRepository.findCompetitionListByNumName(numName);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            dataList.add(getMapFromCompetition(sList.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getCompetitionMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse competitionDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer competitionId = dataRequest.getInteger("competitionId");
        Competition s= null;
        Optional<Competition> oc;
        if(competitionId != null) {
            oc= competitionRepository.findById(competitionId);
            if(oc.isPresent()) {
                s = oc.get();
            }
        }
        if(s != null) {
            competitionRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer competitionId = dataRequest.getInteger("competitionId");
        Competition c= null;
        Optional<Competition> oc;
        if(competitionId != null) {
            oc= competitionRepository.findById(competitionId);
            if(oc.isPresent()) {
                c= oc.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromCompetition(c));
    }

    public DataResponse competitionEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer competitionId = dataRequest.getInteger("competitionId");
        Map form = dataRequest.getData(); //参数获取Map对象
        String competitionName = CommonMethod.getString(form,"competitionName");
//        String student = CommonMethod.getString(form,"student");
        Integer studentId = CommonMethod.getInteger(form,"studentId");
        Competition c= null;
        Student s=null;
        Optional<Competition> oc;
        if(competitionId != null) {
            oc= competitionRepository.findById(competitionId);
            if(oc.isPresent()) {
                c = oc.get();
            }
        }


        if(c == null) {
            competitionId = getNewCompetitionId();
            c = new Competition();
            c.setCompetitionId(competitionId);
            c.setCompetitionName(competitionName);
        }else {
            competitionId = c.getCompetitionId();
        }

        /*Optional<Person> nOp = personRepository.findByNameAndNum(student,num);
        if(!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生不存在，请输入正确姓名");
        }*/

        c.setCompetitionName((String)form.get("competitionName"));
        c.setCompetitionLevel(CommonMethod.getString(form,"competitionLevel"));
        c.setCompetitionTime(CommonMethod.getString(form,"competitionTime"));
        c.setCompetitionPrize(CommonMethod.getString(form,"competitionPrize"));
        Optional<Student> studentOptional=studentRepository.findById(studentId);
        if(studentOptional.isPresent()){
            c.setStudent(studentOptional.get());
        }

        competitionRepository.save(c);
        return CommonMethod.getReturnData(c.getCompetitionId());
    }
}

