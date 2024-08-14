package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.InnovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/innovation")
public class InnovationController {
    @Autowired
    private InnovationService innovationService;

    @PostMapping("/getInnovationList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.getStudentList(dataRequest);
    }

    @PostMapping("/innovationDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationDelete(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.innovationDelete(dataRequest);
    }

    @PostMapping("/getInnovationInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getInnovationInfo(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.getInnovationInfo(dataRequest);
    }

    @PostMapping("/innovationEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return innovationService.innovationEditSave(dataRequest);
    }




}

