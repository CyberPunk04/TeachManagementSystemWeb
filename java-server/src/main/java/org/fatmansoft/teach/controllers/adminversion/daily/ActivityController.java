package org.fatmansoft.teach.controllers.adminversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.service.AdminVersion.Daily.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/activity")

public class ActivityController {
    @Autowired
    private ActivityService activityService;


    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.getStudentItemOptionList(dataRequest);
    }

    @PostMapping("/getActivityList")
    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.getActivityList(dataRequest);
    }

    @PostMapping("/activityDelete")
    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activityDelete(dataRequest);
    }

    @PostMapping("/activitySave")
    public DataResponse activitySave(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activitySave(dataRequest);
    }
}

