package org.fatmansoft.teach.controllers.studentversion.prize;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Prize.StudentVersionPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionPrize")
public class StudentVersionPrizeController {
    @Autowired
    private StudentVersionPrizeService studentVersionPrizeService;

    @PostMapping("/getPrizeList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getPrizeList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionPrizeService.getPrizeList(dataRequest);
    }
    @PostMapping("/prizeDelete")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse prizeDelete(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionPrizeService.prizeDelete(dataRequest);
    }

    @PostMapping("/getPrizeInfo")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getPrizeInfo(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionPrizeService.getPrizeInfo(dataRequest);
    }

    @PostMapping("/prizeEditSave")
    @PreAuthorize(" hasRole('STUDENT')")
    public DataResponse prizeEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionPrizeService.prizeEditSave(dataRequest);
    }

}

