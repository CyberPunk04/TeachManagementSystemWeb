package org.fatmansoft.teach.controllers;


import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.EmailService;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.fatmansoft.teach.util.LoginControlUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.fatmansoft.teach.payload.request.LoginRequest;

import org.fatmansoft.teach.payload.response.JwtResponse;

import org.fatmansoft.teach.security.jwt.JwtUtils;
import org.fatmansoft.teach.security.services.UserDetailsImpl;

/**
 *  AuthController 实现 登录和注册Web服务
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;


    String rightCode = null;//正确的邮箱验证码
    /**
     *  用户登录
     * @param loginRequest   username 登录名  password 密码
     * @return   JwtResponse 用户信息， 该信息再后续的web请求时作为请求头的一部分，用于框架的请求服务权限验证
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Optional<User> op= userRepository.findByUserName(loginRequest.getUsername());
        if(op.isPresent()) {
            User user= op.get();
            user.setLastLoginTime(DateTimeTool.parseDateTime(new Date()));
            Integer count = user.getLoginCount();
            if (count == null)
                count = 1;
            else count += 1;
            user.setLoginCount(count);
            userRepository.save(user);
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPerName(),
                roles.get(0)));
    }


    @PostMapping("/registerUser")
    public DataResponse registerUser(@Valid @RequestBody DataRequest dataRequest) {
        String username = dataRequest.getString("username");
        String password = dataRequest.getString("password");
        String perName = dataRequest.getString("perName");
        String email = dataRequest.getString("email");
        String code = dataRequest.getString("code");
        String role = dataRequest.getString("role");
        UserType ut = null;
        Optional<User> uOp = userRepository.findByUserName(username);
        if(uOp.isPresent()) {
            return CommonMethod.getReturnMessageError("用户已经存在，不能注册！");
        }
        if(!code.equals(rightCode)) {
            return CommonMethod.getReturnMessageError("验证码错误或已过期！");
        }
        Person p = new Person();
        p.setNum(username);
        p.setName(perName);
        p.setEmail(email);
        p.setAvatar("https://s2.loli.net/2023/12/20/e6bSuLdjVEBqIxW.jpg");//默认头像
        if("ADMIN".equals(role)) {
            p.setType("0");
            ut = userTypeRepository.findByName(EUserType.ROLE_ADMIN);
        }else if("STUDENT".equals(role)) {
            p.setType("1");
            ut = userTypeRepository.findByName(EUserType.ROLE_STUDENT);
        }else if("TEACHER".equals(role)) {
            p.setType("2");
            ut = userTypeRepository.findByName(EUserType.ROLE_TEACHER);
        }
        personRepository.saveAndFlush(p);
        User u = new User();
        u.setPerson(p);
        u.setUserType(ut);
        u.setUserName(username);
        u.setPassword(encoder.encode(password));//加密后的密码
        u.setCreateTime(DateTimeTool.parseDateTime(new Date()));
        u.setCreatorId(p.getPersonId());
        u.setLoginCount(0);
        userRepository.saveAndFlush(u);
        if("STUDENT".equals(role)) {
           Student s = new Student();   // 创建实体对象
            s.setPerson(p);
            studentRepository.saveAndFlush(s);  //插入新的Student记录
        }else if("TEACHER".equals(role)) {
            Teacher t = new Teacher();   // 创建实体对象
            t.setPerson(p);
            teacherRepository.saveAndFlush(t);  //插入新的Student记录
        }
        return CommonMethod.getReturnData(LoginControlUtil.getInstance().getValidateCodeDataMap());
    }



    @PostMapping("/registerUser/emailVerify")
    public DataResponse registerUserEmailVerify(@Valid @RequestBody DataRequest dataRequest) throws MessagingException {
        String email = dataRequest.getString("email");
        if (email == null || email.length() == 0){
            return CommonMethod.getReturnMessageError("请输入邮箱");
        }
        //处理邮箱业务
        rightCode= (Math.random()+"").substring(2,8);
        emailService.sendTextEmail(email,"【SDU】SDU账号绑定邮箱","验证码为："+rightCode+"，请在5分钟内完成验证。");
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getValidateCode")
    public DataResponse getValidateCode(@Valid @RequestBody DataRequest dataRequest) {
        return CommonMethod.getReturnData(LoginControlUtil.getInstance().getValidateCodeDataMap());
    }

    @PostMapping("/testValidateInfo")
    public DataResponse testValidateInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer validateCodeId = dataRequest.getInteger("validateCodeId");
        String validateCode = dataRequest.getString("validateCode");
        LoginControlUtil li =  LoginControlUtil.getInstance();
        if(validateCodeId == null || validateCode== null || validateCode.length() == 0) {
            return CommonMethod.getReturnMessageError("验证码为空！");
        }
        String value = li.getValidateCode(validateCodeId);
        if(!validateCode.equals(value))
            return CommonMethod.getReturnMessageError("验证码错位！");
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/resetPassWord")
    public DataResponse resetPassWord(@Valid @RequestBody DataRequest dataRequest) throws MessagingException {
        String perNum = dataRequest.getString("username");
        String email = dataRequest.getString("email");
        Optional<User> op = userRepository.findByUserName(perNum);
        if (!op.isPresent())
            return CommonMethod.getReturnMessageError("账户不存在！");  //通知前端操作正常
        User u = op.get();
        Person p = u.getPerson();
        if(!email.equals(p.getEmail())) {
            return CommonMethod.getReturnMessageError("邮箱不匹配不能重置！");
        }
        //随机生成6位数密码
        String newPassword= (Math.random()+"").substring(2,8);
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
        emailService.sendTextEmail(email,"【SDU】SDU账号重置密码","账号"+perNum + "的新密码为："+newPassword + "。\n" +"请及时重置为强密码");
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
}
