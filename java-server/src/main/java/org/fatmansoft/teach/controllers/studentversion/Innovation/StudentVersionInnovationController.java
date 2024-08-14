package org.fatmansoft.teach.controllers.studentversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Innovation.StudentVersionInnovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionInnovation")
public class StudentVersionInnovationController {
    @Autowired
    private StudentVersionInnovationService studentVersionInnovationService;

    @PostMapping("/getInnovationList")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionInnovationService.getStudentList(dataRequest);
    }

}

