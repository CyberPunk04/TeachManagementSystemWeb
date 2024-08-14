package org.fatmansoft.teach.controllers.studentversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Daily.StudentVersionLeavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionLeaving")
public class StudentVersionLeavingController {
    @Autowired
    private StudentVersionLeavingService studentVersionLeavingService;

    @PostMapping("/getLeavingList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getLeavingList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionLeavingService.getLeavingList(dataRequest);
    }

    @PostMapping("/getLeavingListByNumName")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getLeavingListByNumName(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionLeavingService.getLeavingListByNumName(dataRequest);
    }

    @PostMapping("/leavingDelete")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse leavingDelete(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionLeavingService.leavingDelete(dataRequest);
    }

    @PostMapping("/getLeavingInfo")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getLeavingInfo(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionLeavingService.getLeavingInfo(dataRequest);
    }

    @PostMapping("/leavingEditSave")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse leavingEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionLeavingService.leavingEditSave(dataRequest);
    }

}
