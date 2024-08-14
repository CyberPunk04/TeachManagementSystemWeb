package org.fatmansoft.teach.controllers.adminversion.prize;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.AdminVersion.Prize.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/prize")
public class PrizeController {
    @Autowired
    private PrizeService prizeService;

    @PostMapping("/getPrizeList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStudentList(@Valid @RequestBody DataRequest dataRequest) {
        return prizeService.getStudentList(dataRequest);
    }

    @PostMapping("/prizeDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse prizeDelete(@Valid @RequestBody DataRequest dataRequest) {
        return prizeService.prizeDelete(dataRequest);
    }

    @PostMapping("/getPrizeInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getPrizeInfo(@Valid @RequestBody DataRequest dataRequest) {
        return prizeService.getPrizeInfo(dataRequest);
    }

    @PostMapping("/prizeEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse prizeEditSave(@Valid @RequestBody DataRequest dataRequest) {
        return prizeService.prizeEditSave(dataRequest);
    }
}

