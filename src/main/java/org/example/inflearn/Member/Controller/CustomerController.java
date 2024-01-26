package org.example.inflearn.Member.Controller;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Service.EmailService;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.ReqDtos.CustomerSignUpReq;
import org.example.inflearn.Member.Model.ReqDtos.EmailConfirmReq;
import org.example.inflearn.Member.Model.ReqDtos.LoginReq;
import org.example.inflearn.Member.Model.ReqDtos.CustomerUpdateReq;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity CustomerRead(@PathVariable Long idx)
    {
        return ResponseEntity.ok().body(memberService.CustomerRead(idx));
    }
    // 다수 조회 - Read
    @GetMapping("/customer/list")
    public ResponseEntity CustomerList()
    {
        return ResponseEntity.ok().body(memberService.CustomerList());
    }

    // 로그인 기능
    @PostMapping("/customer/login")
    public ResponseEntity CustomerLogin(@RequestBody LoginReq loginReq)
    {
        return ResponseEntity.ok().body(memberService.CustomerLogin(loginReq));
    }

    // 회원 정보 수정 - Update
    @PutMapping("/customer/update") // token 추가해서 요청
    public ResponseEntity CustomerInfoUpdate(@AuthenticationPrincipal Customer customer, @RequestBody CustomerUpdateReq customerUpdateReq)
    {
        return ResponseEntity.ok().body(memberService.CustomerInfoUpdate(customer, customerUpdateReq));
    }

    //  회원 탈퇴
    @DeleteMapping("/customer/delete")
    public ResponseEntity CustomerDelete(@AuthenticationPrincipal String email, String password)
    {
        memberService.CustomerDelete(email,password);
        return ResponseEntity.ok().body("계정이 삭제 되었습니다.");
    }

    // 이메일 인증
    @GetMapping(value = "/customer/confirm")
    public RedirectView confirm(EmailConfirmReq emailConfirmReq){
        return emailService.verify(emailConfirmReq);
    }
    }
