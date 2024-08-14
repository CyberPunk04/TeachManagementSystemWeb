package org.fatmansoft.teach.controllers.adminversion.Innovation;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Innovation.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lecture")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    @PostMapping("/getLectureList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getLectureList(@Valid @RequestBody DataRequest dataRequest) {
        return lectureService.getLectureList(dataRequest);
    }

    @PostMapping("/lectureDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse lectureDelete(@Valid @RequestBody DataRequest dataRequest) {
        return lectureService.lectureDelete(dataRequest);
    }

    @PostMapping("/getLectureInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getLectureInfo(@Valid @RequestBody DataRequest dataRequest) {
        return lectureService.getLectureInfo(dataRequest);
    }

    @PostMapping("/lectureEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse lectureEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return lectureService.lectureEditSave(dataRequest);
    }
}

