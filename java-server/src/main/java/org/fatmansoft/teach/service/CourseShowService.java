package org.fatmansoft.teach.service.Teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.teaching.CourseSelection;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.teaching.CourseSelectionRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CourseShowService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseSelectionRepository courseSelectionRepository;

    public Map getMapFromCourseSelection(CourseSelection s) {
        Map m = new HashMap();
        Student c;
        Course e;
        if (s == null)
            return m;
        c = s.getStudent();
        if (c == null) {
            return m;
        }
        m.put("student", c.getPerson().getName());
        m.put("courseSelectionId", s.getCourseSelectionId());
        e = s.getCourse();
        if (e == null) {
            return m;
        }
        m.put("course", e.getName());
        //m.put("teacher", e.getTeacher().getPerson().getName());
        return m;
    }

    public DataResponse getCourseSelectionData(@Valid @RequestBody DataRequest dataRequest) {
        /*Integer userId = CommonMethod.getUserId();
        Optional<User> uOp = userRepository.findByUserId(userId);
        User u = uOp.get();
        Optional<Student> sOp= studentRepository.findByPersonPersonId(u.getPerson().getPersonId());
        Student s= sOp.get();
        Integer studentId=s.getStudentId();*/
        Student s=studentRepository.findByPersonNum(CommonMethod.getUsername()).get();
        Integer studentId=s.getStudentId();
        Map data = new HashMap();

        String classTimeAA="周一第一节";
        Optional<CourseSelection> caa=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeAA);
        if(caa.isPresent()){
            CourseSelection aa =caa.get();
            Map infoAA = getMapFromCourseSelection(aa);
            data.put("aa",infoAA);
        }
        String classTimeAB="周一第二节";
        Optional<CourseSelection> cab=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeAB);
        if(cab.isPresent()) {
            CourseSelection ab = cab.get();
            Map infoAB = getMapFromCourseSelection(ab);
            data.put("ab",infoAB);
        }
        String classTimeAC="周一第三节";
        Optional<CourseSelection> cac=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeAC);
        if(cac.isPresent()) {
            CourseSelection ac = cac.get();
            Map infoAC = getMapFromCourseSelection(ac);
            data.put("ac",infoAC);
        }
        String classTimeAD="周一第四节";
        Optional<CourseSelection> cad=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeAD);
        if(cad.isPresent()) {
            CourseSelection ad = cad.get();
            Map infoAD = getMapFromCourseSelection(ad);
            data.put("ad",infoAD);
        }
        String classTimeAE="周一第五节";
        Optional<CourseSelection> cae=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeAE);
        if(cae.isPresent()) {
            CourseSelection ae = cae.get();
            Map infoAE = getMapFromCourseSelection(ae);
            data.put("ae",infoAE);
        }
        String classTimeBA="周二第一节";
        Optional<CourseSelection> cba=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeBA);
        if(cba.isPresent()) {
            CourseSelection ba = cba.get();
            Map infoBA = getMapFromCourseSelection(ba);
            data.put("ba",infoBA);
        }
        String classTimeBB="周二第二节";
        Optional<CourseSelection> cbb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeBB);
        if(cbb.isPresent()) {
            CourseSelection bb = cbb.get();
            Map infoBB = getMapFromCourseSelection(bb);
            data.put("bb",infoBB);
        }
        String classTimeBC="周二第三节";
        Optional<CourseSelection> cbc=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeBC);
        if(cbc.isPresent()) {
            CourseSelection bc = cbc.get();
            Map infoBC = getMapFromCourseSelection(bc);
            data.put("bc",infoBC);
        }
        String classTimeBD="周二第四节";
        Optional<CourseSelection> cbd=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeBD);
        if(cbd.isPresent()) {
            CourseSelection bd = cbd.get();
            Map infoBD = getMapFromCourseSelection(bd);
            data.put("bd",infoBD);
        }
        String classTimeBE="周二第五节";
        Optional<CourseSelection> cbe=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeBE);
        if(cbe.isPresent()) {
            CourseSelection be = cbe.get();
            Map infoBE = getMapFromCourseSelection(be);
            data.put("be",infoBE);
        }
        String classTimeCA="周三第一节";
        Optional<CourseSelection> cca=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeCA);
        if(cca.isPresent()) {
            CourseSelection ca = cca.get();
            Map infoCA = getMapFromCourseSelection(ca);
            data.put("ca",infoCA);
        }
        String classTimeCB="周三第二节";
        Optional<CourseSelection> ccb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeCB);
        if(ccb.isPresent()) {
            CourseSelection cb = ccb.get();
            Map infoCB = getMapFromCourseSelection(cb);
            data.put("cb",infoCB);
        }
        String classTimeCC="周三第三节";
        Optional<CourseSelection> ccc=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeCC);
        if(ccc.isPresent()) {
            CourseSelection cc = ccc.get();
            Map infoCC = getMapFromCourseSelection(cc);
            data.put("cc",infoCC);
        }
        String classTimeCD="周三第四节";
        Optional<CourseSelection> ccd=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeCD);
        if(ccd.isPresent()) {
            CourseSelection cd = ccd.get();
            Map infoCD = getMapFromCourseSelection(cd);
            data.put("cd",infoCD);
        }
        String classTimeCE="周三第五节";
        Optional<CourseSelection> cce=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeCE);
        if(cce.isPresent()) {
            CourseSelection ce = cce.get();
            Map infoCE = getMapFromCourseSelection(ce);
            data.put("ce",infoCE);
        }
        String classTimeDA="周四第一节";
        Optional<CourseSelection> cda=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeDA);
        if(cda.isPresent()) {
            CourseSelection da = cda.get();
            Map infoDA = getMapFromCourseSelection(da);
            data.put("da",infoDA);
        }
        String classTimeDB="周四第二节";
        Optional<CourseSelection> cdb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeDB);
        if(cdb.isPresent()) {
            CourseSelection db = cdb.get();
            Map infoDB = getMapFromCourseSelection(db);
            data.put("db",infoDB);
        }
        String classTimeDC="周四第三节";
        Optional<CourseSelection> cdc=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeDC);
        if(cdc.isPresent()) {
            CourseSelection dc = cdc.get();
            Map infoDC = getMapFromCourseSelection(dc);
            data.put("dc",infoDC);
        }
        String classTimeDD="周四第四节";
        Optional<CourseSelection> cdd=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeDD);
        if(cdd.isPresent()) {
            CourseSelection dd = cdd.get();
            Map infoDD = getMapFromCourseSelection(dd);
            data.put("dd",infoDD);
        }
        String classTimeDE="周四第五节";
        Optional<CourseSelection> cde=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeDE);
        if(cde.isPresent()) {
            CourseSelection de = cde.get();
            Map infoDE = getMapFromCourseSelection(de);
            data.put("de",infoDE);
        }
        String classTimeEA="周五第一节";
        Optional<CourseSelection> cea=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeEA);
        if(cea.isPresent()) {
            CourseSelection ea = cea.get();
            Map infoEA = getMapFromCourseSelection(ea);
            data.put("ea",infoEA);
        }
        String classTimeEB="周五第二节";
        Optional<CourseSelection> ceb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeEB);
        if(ceb.isPresent()) {
            CourseSelection eb = ceb.get();
            Map infoEB = getMapFromCourseSelection(eb);
            data.put("eb",infoEB);
        }
        String classTimeEC="周五第三节";
        Optional<CourseSelection> cec=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeEC);
        if(cec.isPresent()) {
            CourseSelection ec = cec.get();
            Map infoEC = getMapFromCourseSelection(ec);
            data.put("ec",infoEC);
        }
        String classTimeED="周五第四节";
        Optional<CourseSelection> ced=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeED);
        if(ced.isPresent()) {
            CourseSelection ed = ced.get();
            Map infoED = getMapFromCourseSelection(ed);
            data.put("ed",infoED);
        }
        String classTimeEE="周五第五节";
        Optional<CourseSelection> cee=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeEE);
        if(cee.isPresent()) {
            CourseSelection ee = cee.get();
            Map infoEE = getMapFromCourseSelection(ee);
            data.put("ee",infoEE);
        }
        String classTimeFA="周六第一节";
        Optional<CourseSelection> cfa=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeFA);
        if(cfa.isPresent()) {
            CourseSelection fa = cfa.get();
            Map infoFA = getMapFromCourseSelection(fa);
            data.put("fa",infoFA);
        }
        String classTimeFB="周六第二节";
        Optional<CourseSelection> cfb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeFB);
        if(cfb.isPresent()) {
            CourseSelection fb = cfb.get();
            Map infoFB = getMapFromCourseSelection(fb);
            data.put("fb",infoFB);
        }
        String classTimeFC="周六第三节";
        Optional<CourseSelection> cfc=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeFC);
        if(cfc.isPresent()) {
            CourseSelection fc = cfc.get();
            Map infoFC = getMapFromCourseSelection(fc);
            data.put("fc",infoFC);
        }
        String classTimeFD="周六第四节";
        Optional<CourseSelection> cfd=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeFD);
        if(cfd.isPresent()) {
            CourseSelection fd = cfd.get();
            Map infoFD = getMapFromCourseSelection(fd);
            data.put("fd",infoFD);
        }
        String classTimeFE="周六第五节";
        Optional<CourseSelection> cfe=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeFE);
        if(cfe.isPresent()) {
            CourseSelection fe = cfe.get();
            Map infoFE = getMapFromCourseSelection(fe);
            data.put("fe",infoFE);
        }
        String classTimeGA="周日第一节";
        Optional<CourseSelection> cga=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeGA);
        if(cga.isPresent()) {
            CourseSelection ga = cga.get();
            Map infoGA = getMapFromCourseSelection(ga);
            data.put("ga",infoGA);
        }
        String classTimeGB="周日第二节";
        Optional<CourseSelection> cgb=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeGB);
        if(cgb.isPresent()) {
            CourseSelection gb = cgb.get();
            Map infoGB = getMapFromCourseSelection(gb);
            data.put("gb",infoGB);
        }
        String classTimeGC="周日第三节";
        Optional<CourseSelection> cgc=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeGC);
        if(cgc.isPresent()) {
            CourseSelection gc = cgc.get();
            Map infoGC = getMapFromCourseSelection(gc);
            data.put("gc",infoGC);
        }
        String classTimeGD="周日第四节";
        Optional<CourseSelection> cgd=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeGD);
        if(cgd.isPresent()) {
            CourseSelection gd = cgd.get();
            Map infoGD = getMapFromCourseSelection(gd);
            data.put("gd",infoGD);
        }
        String classTimeGE="周日第五节";
        Optional<CourseSelection> cge=courseSelectionRepository.findByStudentStudentIdAndCourseClassTime(studentId,classTimeGE);
        if(cge.isPresent()) {
            CourseSelection ge = cge.get();
            Map infoGE = getMapFromCourseSelection(ge);
            data.put("ge",infoGE);
        }

        return CommonMethod.getReturnData(data);
    }

}