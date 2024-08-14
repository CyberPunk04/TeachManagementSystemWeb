package org.fatmansoft.teach.service.Teaching;


import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.repository.teaching.CourseSelectionRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Service
public class TeacherScoreService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseSelectionRepository courseSelectionRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    public synchronized Integer getNewScoreId(){  //synchronized 同步方法
        Integer  id = scoreRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    }

    public Map getMapFromCourseSelection(CourseSelection c) {
        Map m = new HashMap();
        Student s;
        Course e;
        if(c == null)
            return m;
        m.put("courseSelectionId",c.getCourseSelectionId());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        m.put("studentId",s.getStudentId());
        m.put("num",s.getPerson().getNum());
        m.put("className",s.getClassName());
        e=c.getCourse();
        if(e==null){
            return m;
        }
        Optional<Score> ss = scoreRepository.findByCourseCourseIdStudentStudentId(c.getCourse().getCourseId(),c.getStudent().getStudentId());
        if(ss.isPresent()) {
            Score sss =ss.get();
            m.put("course", e.getName());
            m.put("courseId", e.getCourseId());
            m.put("courseNum",e.getNum());
            m.put("teacher", e.getTeacher().getPerson().getName());
            m.put("answer", "已录入分数");
            m.put("mark",sss.getMark() );
        }
        return m;
    }

    public Map getNewMapFromCourseSelection(CourseSelection c) {
        Map m = new HashMap();
        Student s;
        Course e;
        if(c == null)
            return m;
        m.put("courseSelectionId",c.getCourseSelectionId());
        s=c.getStudent();
        if(s==null){
            return m;
        }
        m.put("student",s.getPerson().getName());
        m.put("studentId",s.getStudentId());
        m.put("num",s.getPerson().getNum());
        m.put("className",s.getClassName());
        e=c.getCourse();
        if(e==null){
            return m;
        }
        m.put("course",e.getName());
        m.put("courseId",e.getCourseId());
        m.put("courseNum",e.getNum());
        m.put("teacher",e.getTeacher().getPerson().getName());
        m.put("answer","未录入分数");
        m.put("mark",null);
        return m;
    }

    public List getCourseSelectionMapList(String numName) {
        Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Teacher> sOp= teacherRepository.findByPersonPersonId(u.getPerson().getPersonId());
        Teacher t= sOp.get();
        Integer teacherId=t.getTeacherId();


        List dataList = new ArrayList();
        List<CourseSelection> sList =  courseSelectionRepository.findCourseSelectionListByNumNameAndTeacher(numName,teacherId);
        if(sList == null || sList.size() == 0)
            return dataList;
        for(int i = 0; i < sList.size();i++) {
            Student s=sList.get(i).getStudent();
            Integer ss=s.getStudentId();
            Course c=sList.get(i).getCourse();
            Integer cc=c.getCourseId();
            Optional<Score> mm= scoreRepository.findByCourseAndStudent(cc, ss);
            if(mm.isEmpty()){
                dataList.add(getNewMapFromCourseSelection(sList.get(i)));}
            else{dataList.add(getMapFromCourseSelection(sList.get(i)));}

        }
        return dataList;
    }

    public DataResponse getCourseSelectionList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getCourseSelectionMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse getCourseSelectionInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseSelectionId = dataRequest.getInteger("courseSelectionId");
        CourseSelection c= null;
        Optional<CourseSelection> oc;
        Optional<Score> mm = Optional.empty();
        if(courseSelectionId != null) {
            oc = courseSelectionRepository.findById(courseSelectionId);
            if (oc.isPresent()) {
                c = oc.get();
            }
        }
        if(c!=null) {
            Student s = c.getStudent();
            Course cc = c.getCourse();
            Integer ss = s.getStudentId();
            Integer ccc = cc.getCourseId();
            mm = scoreRepository.findByCourseAndStudent(ccc, ss);
        }

        if(mm.isEmpty()){
            return CommonMethod.getReturnData(getNewMapFromCourseSelection(c));}
        else{return CommonMethod.getReturnData(getMapFromCourseSelection(c));}
    }

    public DataResponse scoreSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("studentId");
        Integer courseId = dataRequest.getInteger("courseId");
        Integer mark = dataRequest.getInteger("mark");
        Integer scoreId = dataRequest.getInteger("scoreId");
        Optional<Score> op;
        Score s = null;
        op = scoreRepository.findByCourseAndStudent(courseId,studentId);
            if(op.isPresent())
                s = op.get();

        if(s == null) {
            s = new Score();
            s.setStudent(studentRepository.findById(studentId).get());
            s.setCourse(courseRepository.findById(courseId).get());
        }
        s.setMark(mark);
        scoreRepository.save(s);
        return CommonMethod.getReturnMessageOK();
    }

    public void updateRanking() throws SQLException {
        // 连接数据库
        String url = "jdbc:sqlite:/teach-web/data.db ";
        Connection conn = DriverManager.getConnection(url);

        try {
            // 执行查询并排序
            String sql = "SELECT course_id, mark, dense_rank() OVER (PARTITION BY course_id ORDER BY mark DESC) AS ranking FROM score";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // update the ranking field in the database
            while (rs.next()) {
                String course = rs.getString("course_id");
                int score = rs.getInt("mark");
                int ranking = rs.getInt("ranking");

                // update the ranking field for this row
                String updateSql = "UPDATE score SET ranking=? WHERE course_id=? AND mark=?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, ranking);
                updateStmt.setString(2, course);
                updateStmt.setInt(3, score);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            // 处理异常
        } finally {
            // 关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // 处理异常
                }
            }
        }
    }

    public void calculateAverageMarks() {
        try {
            // 连接数据库
            String url = "jdbc:sqlite:/teach-web/data.db";
            Connection conn = DriverManager.getConnection(url);

            // 使用PreparedStatement查询，并使用ResultSet处理结果
            String query = "SELECT student_id, AVG(mark) AS avg_mark FROM score GROUP BY student_id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // 更新平均分数字段
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                BigDecimal avgMark = rs.getBigDecimal("avg_mark").setScale(2, BigDecimal.ROUND_HALF_UP);

                String updateQuery = "UPDATE score SET avg_mark = ? WHERE student_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setBigDecimal(1, avgMark);
                updateStmt.setString(2, studentId);

                updateStmt.executeUpdate();
                updateStmt.close();
            }

            // 关闭所有资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

