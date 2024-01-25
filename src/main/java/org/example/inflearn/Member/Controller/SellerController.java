package org.example.inflearn.Member.Controller;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Service.EmailService;
import org.example.inflearn.Member.Model.ReqDtos.*;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class SellerController {

    private final MemberService memberService;
    private final EmailService emailService;

    // 계정 생성 - Create
    @PostMapping("/seller/signup")
    public ResponseEntity SellerSignUp(@RequestBody SellerSignUpReq sellerSignUpReq)
    {
        return ResponseEntity.ok().body(memberService.SellerSignUp(sellerSignUpReq));
    }

    // 단일 조회 - Read
    @GetMapping("/seller/{idx}")
    public ResponseEntity SellerRead(@PathVariable Long idx)
    {
        return ResponseEntity.ok().body(memberService.SellerRead(idx));
    }
    // 다수 조회 - Read
    @GetMapping("/seller/list")
    public ResponseEntity SellerList()
    {
        return ResponseEntity.ok().body(memberService.SellerList());
    }

    // 로그인 기능
    @PostMapping("/seller/login")
    public ResponseEntity SellerLogin(@RequestBody LoginReq loginReq)
    {
        return ResponseEntity.ok().body(memberService.SellerLogin(loginReq));
    }

    // 회원 정보 수정 - Update
    @PutMapping("/seller/update") // token 추가해서 요청
    public ResponseEntity SellerInfoUpdate(@AuthenticationPrincipal String email, @RequestBody SellerUpdateReq sellerUpdateReq)
    {
        return ResponseEntity.ok().body(memberService.SellerInfoUpdate(email, sellerUpdateReq));
    }

    //  회원 탈퇴
    @DeleteMapping("/seller/delete")
    public ResponseEntity SellerDelete(@AuthenticationPrincipal String email, String password)
    {
        memberService.SellerDelete(email,password);
        return ResponseEntity.ok().body("계정이 삭제 되었습니다.");
    }

    // 이메일 인증
    @GetMapping(value = "/seller/confirm")
    public RedirectView confirm(EmailConfirmReq emailConfirmReq){
        return emailService.verify(emailConfirmReq);
    }
}
