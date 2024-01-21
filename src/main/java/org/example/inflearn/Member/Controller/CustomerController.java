package org.example.inflearn.Member.Controller;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Service.EmailService;
import org.example.inflearn.Member.Model.ReqDtos.CustomerSignUpReq;
import org.example.inflearn.Member.Model.ReqDtos.EmailConfirmReq;
import org.example.inflearn.Member.Model.ResDtos.CustomerSignUpRes;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final MemberService memberService;
    private final EmailService emailService;

    // 계정 생성 - Create
    @PostMapping("/customer/signup")
    public ResponseEntity CustomerSignUp(@RequestBody CustomerSignUpReq customerSignUpReq)
    {
        return ResponseEntity.ok().body(memberService.CustomerSignUp(customerSignUpReq));
    }

    // 단일 조회 - Read
    @GetMapping("/customer/{idx}")
    public void CustomerRead(@PathVariable int idx)
    {

    }
    // 다수 조회 - Read
    @GetMapping("/customer/signup")
    public void CustomerList()
    {

    }

    // 로그인 기능
    @PostMapping("/customer/login")
    public void CustomerLogin()
    {

    }

    // 회원 정보 수정 - Update
    @PutMapping("/customer/update")
    public void CustomerInfoUpdate()
    {

    }

    //  회원 탈퇴
    @DeleteMapping("/customer/delete")
    public void CustomerDelete()
    {

    }

    // 이메일 인증
    @GetMapping(value = "/customer/confirm")
    public RedirectView confirm(EmailConfirmReq emailConfirmReq){
        return emailService.verify(emailConfirmReq);
    }
    }
