package org.fatmansoft.teach.controllers.teaching;


import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.Teaching.TeacherScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacherScore")
public class TeacherScoreController {
    @Autowired
    private TeacherScoreService teacherScoreService;

    @PostMapping("/getCourseSelectionList")
    @PreAuthorize("hasRole('TEACHER')")
    public DataResponse getCourseSelectionList(@Valid @RequestBody DataRequest dataRequest) {
        return teacherScoreService.getCourseSelectionList(dataRequest);
    }

    @PostMapping("/getCourseSelectionInfo")
    @PreAuthorize("hasRole('TEACHER')")
    public DataResponse getCourseSelectionInfo(@Valid @RequestBody DataRequest dataRequest) {
        return teacherScoreService.getCourseSelectionInfo(dataRequest);
    }

    @PostMapping("/scoreSave")
    @PreAuthorize(" hasRole('TEACHER')")
    public DataResponse scoreSave(@Valid @RequestBody DataRequest dataRequest) {
        return teacherScoreService.scoreSave(dataRequest);
    }

}
