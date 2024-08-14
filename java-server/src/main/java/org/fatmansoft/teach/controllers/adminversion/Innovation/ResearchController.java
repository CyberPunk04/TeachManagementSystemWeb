package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/research")
public class ResearchController {
    @Autowired
    private ResearchService researchService;

    @PostMapping("/getResearchList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return researchService.getStudentList(dataRequest);
    }

    @PostMapping("/researchDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse researchDelete(@Valid @RequestBody DataRequest dataRequest) {
        return researchService.researchDelete(dataRequest);
    }

    @PostMapping("/getResearchInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        return researchService.getStudentInfo(dataRequest);
    }

    @PostMapping("/researchEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse researchEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return researchService.researchEditSave(dataRequest);
    }

}

