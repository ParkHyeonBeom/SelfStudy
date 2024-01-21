package org.example.inflearn.Member.Controller;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Member.Service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SellerController {

    private final MemberService memberService;

    // 계정 생성 - Create
    @PostMapping("/seller/signup")
    public void SellerSignUp()
    {

    }

    // 단일 조회 - Read
    @GetMapping("/seller/read/{idx}")
    public void SellerRead(@PathVariable int idx)
    {

    }
    // 다수 조회 - Read
    @GetMapping("/seller/list")
    public void SellerList()
    {

    }

    // 로그인 기능
    @PostMapping("/seller/login")
    public void SellerLogin()
    {

    }

    // 회원 정보 수정 - Update
    @PutMapping("/seller/update")
    public void SellerInfoUpdate()
    {

    }

    //  회원 탈퇴
    @DeleteMapping("/seller/delete")
    public void SellerDelete()
    {

    }
}
