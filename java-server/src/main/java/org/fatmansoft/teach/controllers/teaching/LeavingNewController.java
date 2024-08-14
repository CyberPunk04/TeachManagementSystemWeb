package org.fatmansoft.teach.controllers.teaching;

import org.fatmansoft.teach.models.Daily.Leaving;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.LeavingRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.AdminVersion.Daily.LeavingService;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leavingNew")

public class LeavingNewController {
    @Autowired
    private LeavingService leavingService;
    @Autowired
    private LeavingRepository leavingRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/leavingSaveA")
    public DataResponse leavingSaveA(@Valid @RequestBody DataRequest dataRequest) {

        Integer leavingId = dataRequest.getInteger("leavingId");
        String approval = "true";
        String answer = "true";
        Optional<Leaving> op;
        Leaving l = null;
        if(leavingId != null) {
            op= leavingRepository.findByLeavingId(leavingId);
            if(op.isPresent())
                l = op.get();
        }

        l.setApproval(approval);
        l.setAnswer(answer);
        leavingRepository.saveAndFlush(l);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/leavingSaveB")
    public DataResponse leavingSaveB(@Valid @RequestBody DataRequest dataRequest) {

        Integer leavingId = dataRequest.getInteger("leavingId");
        String approval = "true";
        String answer = "false";
        Optional<Leaving> op;
        Leaving l = null;
        if(leavingId != null) {
            op= leavingRepository.findByLeavingId(leavingId);
            if(op.isPresent())
                l = op.get();
        }

        l.setApproval(approval);
        l.setAnswer(answer);
        leavingRepository.saveAndFlush(l);
        return CommonMethod.getReturnMessageOK();
    }

}