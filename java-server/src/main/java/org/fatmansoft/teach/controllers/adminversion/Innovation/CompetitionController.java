package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/competition")
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;

    @PostMapping("/getCompetitionList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return competitionService.getStudentList(dataRequest);
    }

    @PostMapping("/competitionDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse competitionDelete(@Valid @RequestBody DataRequest dataRequest) {
        return competitionService.competitionDelete(dataRequest);
    }
    @PostMapping("/getCompetitionInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        return competitionService.getStudentInfo(dataRequest);
    }

    @PostMapping("/competitionEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse competitionEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return competitionService.competitionEditSave(dataRequest);
    }

}

