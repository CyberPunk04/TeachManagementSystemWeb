package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.Teaching.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacherCourse")

public class TeacherCourseController {
    @Value("${attach.folder}")    //环境配置变量获取
    private String attachFolder;  //服务器端数据存储
    @Autowired
    private TeacherCourseService teacherCourseService;

    @PostMapping("/getCourseList")
    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        return teacherCourseService.getCourseList(dataRequest);
    }

    @PostMapping("/getCourseInfo")
    public DataResponse getCourseInfo(@Valid @RequestBody DataRequest dataRequest) {
        return teacherCourseService.getCourseInfo(dataRequest);
    }

    @PostMapping(path = "/uploadPdf")
    public DataResponse uploadPhoto(@RequestBody byte[] barr,
                                    @RequestParam(name = "uploader") String uploader,
                                    @RequestParam(name = "remoteFile") String remoteFile,
                                    @RequestParam(name = "fileName") String fileName)  {
        return teacherCourseService.uploadPhoto(barr,uploader,remoteFile,fileName);
    }

}
