package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.Teaching.CourseSelectionService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courseSelection")
public class CourseSelectionController {
    @Autowired
    private CourseSelectionService courseSelectionService;

    @PostMapping("/getCourseList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectionService.getStudentList(dataRequest);
    }

    @PostMapping("/getCourseInfo")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectionService.getStudentInfo(dataRequest);
    }

    @PostMapping("/courseSave")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse courseSave(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectionService.courseSave(dataRequest);
    }

    @PostMapping("/courseSelectionDelete")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectionService.courseDelete(dataRequest);
    }

    @PostMapping("/getCourseSelectionList")
    public DataResponse getCourseSelectionList(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectionService.getCourseSelectionList(dataRequest);
    }

}
