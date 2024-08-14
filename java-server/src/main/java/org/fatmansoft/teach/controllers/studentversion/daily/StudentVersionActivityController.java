package org.fatmansoft.teach.controllers.studentversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Daily.StudentVersionActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionActivity")
public class StudentVersionActivityController {
    @Autowired
    private StudentVersionActivityService studentVersionActivityService;

    @PostMapping("/getActivityList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionActivityService.getActivityList(dataRequest);
    }

}
