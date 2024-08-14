package org.fatmansoft.teach.controllers.studentversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Innovation.StudentVersionInternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionInternship")
public class StudentVersionInternshipController {
    @Autowired
    private StudentVersionInternshipService studentVersionInternshipService;

    @PostMapping("/getInternshipList")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public DataResponse getInternshipList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionInternshipService.getInternshipList(dataRequest);
    }

}
