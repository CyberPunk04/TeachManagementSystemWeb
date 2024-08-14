package org.fatmansoft.teach.controllers.adminversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.FeeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.service.AdminVersion.Daily.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fee")

public class FeeController {
    @Autowired
    private FeeRepository feeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FeeService feeService;


    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        return feeService.getStudentItemOptionList(dataRequest);
    }

    @PostMapping("/getFeeList")
    public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
        return feeService.getFeeList(dataRequest);
    }

    @PostMapping("/feeDelete")
    public DataResponse feeDelete(@Valid @RequestBody DataRequest dataRequest) {
        return feeService.feeDelete(dataRequest);
    }

    @PostMapping("/feeSave")
    public DataResponse feeSave(@Valid @RequestBody DataRequest dataRequest) {
        return feeService.feeSave(dataRequest);
    }
}

