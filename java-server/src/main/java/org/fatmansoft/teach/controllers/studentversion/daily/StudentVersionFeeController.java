package org.fatmansoft.teach.controllers.studentversion.daily;

import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.service.StudentVersion.Daily.StudentVersionFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/studentVersionFee")
public class StudentVersionFeeController {
    @Autowired
    private StudentVersionFeeService studentVersionFeeService;

    @PostMapping("/getFeeList")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
        return studentVersionFeeService.getFeeList(dataRequest);
    }

}
