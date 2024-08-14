package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.InnovationService;
import org.fatmansoft.teach.service.AdminVersion.Innovation.InternshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/internship")
public class InternshipController {
    @Autowired
    private InternshipService innovationService;

    @PostMapping("/getInternshipList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.getStudentList(dataRequest);
    }

    @PostMapping("/internshipDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse internshipDelete(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.internshipDelete(dataRequest);
    }

    @PostMapping("/getInternshipInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getInternshipInfo(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.getInternshipInfo(dataRequest);
    }

    @PostMapping("/internshipEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse internshipEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.internshipEditSave(dataRequest);
    }
}
