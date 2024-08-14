package org.fatmansoft.teach.service.StudentVersion.Innovation;

import org.fatmansoft.teach.models.Innovation.Competition;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CompetitionRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentVersionCompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private StudentRepository studentRepository;

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
        return m;
    }

    public List getCompetitionMapList(Integer studentId) {
        List dataList = new ArrayList();
        List<Competition> s=competitionRepository.findByStudentStudentId(studentId);
        if(s == null || s.size() == 0)
            return dataList;
        for(int i = 0; i < s.size();i++) {
            dataList.add(getMapFromCompetition(s.get(i)));
        }
        return dataList;
    }

    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        List dataList = getCompetitionMapList(studentId);
        return CommonMethod.getReturnData(dataList);
    }

}

