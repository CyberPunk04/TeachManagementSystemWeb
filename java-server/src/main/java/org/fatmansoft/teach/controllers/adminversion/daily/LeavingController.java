package org.fatmansoft.teach.controllers.adminversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.service.AdminVersion.Daily.LeavingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leaving")

public class LeavingController {
    @Autowired
    private LeavingService leavingService;


    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        return leavingService.getStudentItemOptionList(dataRequest);
    }

    @PostMapping("/getLeavingList")
    public DataResponse getLeavingList(@Valid @RequestBody DataRequest dataRequest) {
        return leavingService.getLeavingList(dataRequest);
    }

    @PostMapping("/leavingDelete")
    public DataResponse leavingDelete(@Valid @RequestBody DataRequest dataRequest) {
        return leavingService.leavingDelete(dataRequest);
    }

    @PostMapping("/leavingSave")
    public DataResponse leavingSave(@Valid @RequestBody DataRequest dataRequest) {
        return leavingService.leavingSave(dataRequest);
    }


}

