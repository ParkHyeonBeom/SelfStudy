package org.example.inflearn.Member.Service;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Model.SendEmailReq;
import org.example.inflearn.Email.Service.EmailService;
import org.example.inflearn.Jwt.JwtUtils;
import org.example.inflearn.Member.BaseResponse;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.Entity.Seller;
import org.example.inflearn.Member.Model.ReqDtos.CustomerSignUpReq;
import org.example.inflearn.Member.Model.ResDtos.CustomerSignUpRes;
import org.example.inflearn.Member.Repository.CustomerRepository;
import org.example.inflearn.Member.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.example.inflearn.Grade.Grade.Bronze;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final EmailService emailService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private int expiredTimeMs;

    // 계정 생성 - Create
    public BaseResponse<CustomerSignUpRes> CustomerSignUp(CustomerSignUpReq customerSignUpReq)
    {

        // 1. 이메일을 통해 이미 존재하는 회원인지 확인
        if(customerRepository.findCustomerByCustomerEmail(customerSignUpReq.getCustomerEmail()).isPresent())
        {
            return BaseResponse.failResponse(7000,"중복된 회원이 있습니다.");
        }

        // 2. 존재하지않는 회원이라면 Customer Entity에 저장하고
        Customer customer = customerRepository.save(Customer.builder()
                        .customerName(customerSignUpReq.getCustomerName())
                        .customerEmail(customerSignUpReq.getCustomerEmail())
                        .customerPassword(customerSignUpReq.getCustomerPassword())
                        .customerAddress(customerSignUpReq.getCustomerAddress())
                        .customerPNum(customerSignUpReq.getCustomerPNum())
                        .customerGrade(Bronze)
                        .customerAuthority("Customer")
                        .socialLogin(false)
                        .status(false)
                .build());

        // 3. AccessToken을 생성하여
        String accessToken = JwtUtils.generateAccessToken(customer, secretKey, expiredTimeMs);

        // 4. 이메일에 포함시켜 사용자에게 전달하여 이메일 인증을 요청
        SendEmailReq sendEmailReq = SendEmailReq.builder()
                .email(customer.getCustomerEmail())
                .authority(customer.getCustomerAuthority())
                .accessToken(accessToken)
                .build();

        // 5. 이메일 전송
        emailService.sendEmail(sendEmailReq);

        // 6. 응답 Dto 생성을 위한 과정
        Optional<Customer> result = customerRepository.findCustomerByCustomerEmail(customer.getCustomerEmail());

        if (result.isPresent()){
            customer = result.get();
        }

        // TODO : 응답 Dto 수정 필요 ! Entity의 모든 정보를 전송하고 있기때문에 Entity를 응답해주는것과 동일한 상황
        CustomerSignUpRes consumerSignupRes = CustomerSignUpRes.builder()
                .customerName(customer.getCustomerEmail())
                .customerEmail(customer.getCustomerEmail())
                .customerPassword(customer.getCustomerPassword())
                .customerAddress(customer.getCustomerAddress())
                .customerPNum(customer.getCustomerPNum())
                .customerGrade(customer.getCustomerGrade())
                .customerAuthority(customer.getCustomerAuthority())
                .socialLogin(customer.getSocialLogin())
                .status(customer.getStatus())
                .build();

        return BaseResponse.successResponse("회원가입이 성공적으로 완료되었습니다.",consumerSignupRes);
    }

    // 단일 조회 - Read
    public void CustomerRead()
    {

    }
    // 다수 조회 - Read
    public void CustomerList()
    {

    }

    // 로그인 기능
    public void CustomerLogin()
    {

    }

    // 회원 정보 수정 - Update
    public void CustomerInfoUpdate()
    {

    }

    //  회원 탈퇴
    public void CustomerDelete()
    {

    }

    public void SellerSignUp()
    {

    }

    // 단일 조회 - Read
    public void SellerRead()
    {

    }
    // 다수 조회 - Read
    public void SellerList()
    {

    }

    // 로그인 기능
    public void SellerLogin()
    {

    }

    // 회원 정보 수정 - Update
    public void SellerInfoUpdate()
    {

    }

    //  회원 탈퇴
    public void SellerDelete()
    {

    }

    public Customer getCustomerByConsumerId(String email)
    {
        Optional<Customer> customer = customerRepository.findCustomerByCustomerEmail(email);

        if(customer.isPresent()) {
        return customer.get();
        }
        return null;
    }

    public Seller getSellerBySellerId(String email)
    {
        Optional<Seller> seller = sellerRepository.findSellerBySellerEmail(email);

        if(seller.isPresent()) {
            return seller.get();
        }
        return null;
    }

}
