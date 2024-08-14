package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.Teaching.CourseShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courseShow")
public class CourseShowController {
    @Autowired
    private CourseShowService courseShowService;

    @PostMapping("/getCourseSelectionData")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getCourseSelectionData(@Valid @RequestBody DataRequest dataRequest) {
        return courseShowService.getCourseSelectionData(dataRequest);
    }

}