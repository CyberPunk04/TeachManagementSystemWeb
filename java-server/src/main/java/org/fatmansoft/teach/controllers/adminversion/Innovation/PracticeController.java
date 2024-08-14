package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/practice")
public class PracticeController {
    @Autowired
    private PracticeService practiceService;

    @PostMapping("/getPracticeList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.getStudentList(dataRequest);
    }

    @PostMapping("/practiceDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse practiceDelete(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceDelete(dataRequest);
    }

    @PostMapping("/getPracticeInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentInfo(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.getStudentInfo(dataRequest);
    }

    @PostMapping("/practiceEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse practiceEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceEditSave(dataRequest);
    }
}
