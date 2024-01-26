package org.example.inflearn.Member.Service;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Model.EmailVerify;
import org.example.inflearn.Email.Model.SendEmailReq;
import org.example.inflearn.Email.Repository.EmailVerifyRepository;
import org.example.inflearn.Email.Service.EmailService;
import org.example.inflearn.Jwt.JwtUtils;
import org.example.inflearn.Member.BaseResponse;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.Entity.Seller;
import org.example.inflearn.Member.Model.ReqDtos.*;
import org.example.inflearn.Member.Model.ResDtos.*;
import org.example.inflearn.Member.Repository.CustomerRepository;
import org.example.inflearn.Member.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.inflearn.Grade.Grade.Silver;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerifyRepository emailVerifyRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private int expiredTimeMs;

    // 계정 생성 - Create
    public BaseResponse<CustomerSignUpRes> CustomerSignUp(CustomerSignUpReq customerSignUpReq) {

        // 1. 이메일을 통해 이미 존재하는 회원인지 확인
        if (customerRepository.findByCustomerEmail(customerSignUpReq.getCustomerEmail()).isPresent()) {
            return BaseResponse.failResponse(7000, "중복된 회원이 있습니다.");
        }

        // 2. 존재하지않는 회원이라면 Customer Entity에 저장하고
        Customer customer = customerRepository.save(Customer.builder()
                .customerName(customerSignUpReq.getCustomerName())
                .customerEmail(customerSignUpReq.getCustomerEmail())
                .customerPassword(passwordEncoder.encode(customerSignUpReq.getCustomerPassword()))
                .customerAddress(customerSignUpReq.getCustomerAddress())
                .customerPNum(customerSignUpReq.getCustomerPNum())
                .customerGrade(Silver)
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
        Optional<Customer> result = customerRepository.findByCustomerEmail(customer.getCustomerEmail());

        if (result.isPresent()) {
            customer = result.get();
        }

        // TODO : 응답 Dto 수정 필요 ! Entity의 모든 정보를 전송하고 있기때문에 Entity를 응답해주는것과 동일한 상황
        CustomerSignUpRes consumerSignupRes = CustomerSignUpRes.builder()
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .customerPassword(customer.getCustomerPassword())
                .customerAddress(customer.getCustomerAddress())
                .customerPNum(customer.getCustomerPNum())
                .customerGrade(customer.getCustomerGrade())
                .customerAuthority(customer.getCustomerAuthority())
                .socialLogin(customer.getSocialLogin())
                .status(customer.getStatus())
                .build();

        return BaseResponse.successResponse("이메일 인증 대기중...", consumerSignupRes);
    }

    // 단일 조회 - Read
    public BaseResponse<CustomerReadRes> CustomerRead(Long idx) {
        Optional<Customer> customer = customerRepository.findById(idx);

        if (customer.isPresent()) {
            Customer customerInfo = customer.get();
            CustomerReadRes customerReadRes = CustomerReadRes
                    .builder()
                    .customerName(customerInfo.getCustomerName())
                    .customerEmail(customerInfo.getCustomerEmail())
                    .customerAddress(customerInfo.getCustomerAddress())
                    .customerPNum(customerInfo.getCustomerPNum())
                    .customerGrade(customerInfo.getCustomerGrade())
                    .status(customerInfo.getStatus())
                    .build();
            return BaseResponse.successResponse("요청하신 회원의 정보입니다.", customerReadRes);
        }
        return BaseResponse.failResponse(7000, "요청하신 회원은 가입되어 있지 않습니다.");
    }

    // 다수 조회 - Read
    public BaseResponse<Object> CustomerList() {
        List<Customer> customerList = customerRepository.findAll();

        List<CustomerReadRes> customerReadResList = new ArrayList<>();

        for (Customer customer : customerList) {

            CustomerReadRes customerReadRes = CustomerReadRes
                    .builder()
                    .customerName(customer.getCustomerName())
                    .customerEmail(customer.getCustomerEmail())
                    .customerEmail(customer.getCustomerEmail())
                    .customerAddress(customer.getCustomerAddress())
                    .customerPNum(customer.getCustomerPNum())
                    .customerGrade(customer.getCustomerGrade())
                    .status(customer.getStatus())
                    .build();

            customerReadResList.add(customerReadRes);
        }

        return BaseResponse.successResponse("요청하신 전체 회원의 정보입니다.", customerReadResList);
    }

    // 로그인 기능
    public BaseResponse<LoginRes> CustomerLogin(LoginReq customerLoginReq) {
        LoginRes loginRes = null;
        Optional<Customer> customer = customerRepository.findByCustomerEmail(customerLoginReq.getEmail());
        if (customer.isEmpty()) {
            return BaseResponse.failResponse(7000, "가입되지 않은 회원입니다.");
        } else if (customer.isPresent() && passwordEncoder.matches(customerLoginReq.getPassword(), customer.get().getPassword()))
            ;
        {
            loginRes = LoginRes.builder()
                    .jwtToken(JwtUtils.generateAccessToken(customer.get(), secretKey, expiredTimeMs))
                    .build();

        }
        return BaseResponse.successResponse("정상적으로 로그인 되었습니다.", loginRes);
    }

    // 회원 정보 수정 - Update
    public BaseResponse<CustomerUpdateRes> CustomerInfoUpdate(Customer customer, CustomerUpdateReq customerUpdateReq) {
        Optional<Customer> customer2 = customerRepository.findByCustomerEmail(customer.getUsername());
        if (customer2.isPresent()) {
            System.out.println("인증된 접근입니다.");
        } else throw new RuntimeException("비 인증된 접근입니다.");

        Customer customer3 = customer2.get();

        if (customerUpdateReq.getCustomerPassword() != null) {
            customer3.setCustomerPassword(passwordEncoder.encode(customerUpdateReq.getCustomerPassword()));
        }
        if (customerUpdateReq.getCustomerAddress() != null) {
            customer3.setCustomerAddress(customerUpdateReq.getCustomerAddress());
        }
        if (customerUpdateReq.getCustomerPNum() != null) {
            customer3.setCustomerPNum(customerUpdateReq.getCustomerPNum());
        }

        Customer result = customerRepository.save(customer3);

        CustomerUpdateRes customerUpdateRes = CustomerUpdateRes.builder()
                .customerName(result.getCustomerName())
                .customerEmail(result.getCustomerEmail())
                .customerPassword(result.getCustomerPassword())
                .customerAddress(result.getCustomerAddress())
                .customerPNum(result.getCustomerPNum())
                .build();

        return BaseResponse.successResponse("요청하신 정보 수정이 완료되었습니다.", customerUpdateRes);
    }

    //  회원 탈퇴
    public void CustomerDelete(String email, String password) {
        Optional<Customer> customer2 = customerRepository.findByCustomerEmail(email);
        Optional<EmailVerify> emailVerify = emailVerifyRepository.findByEmail(email);
        if (customer2.isPresent()) {
            if (emailVerify.isPresent() && passwordEncoder.matches(password, customer2.get().getCustomerPassword())) {
                customerRepository.delete(customer2.get());
                emailVerifyRepository.delete(emailVerify.get());
            }
        } else throw new RuntimeException("비 인증된 접근입니다.");


    }

    public BaseResponse<SellerSignUpRes> SellerSignUp(SellerSignUpReq sellerSignUpReq) {
        if (sellerRepository.findSellerBySellerEmail(sellerSignUpReq.getSellerEmail()).isPresent()) {
            return BaseResponse.failResponse(7000, "중복된 회원이 있습니다.");
        }

        // 2. 존재하지않는 회원이라면 Customer Entity에 저장하고
        Seller seller = sellerRepository.save(Seller.builder()
                .sellerName(sellerSignUpReq.getSellerName())
                .sellerEmail(sellerSignUpReq.getSellerEmail())
                .sellerPassword(passwordEncoder.encode(sellerSignUpReq.getSellerPassword()))
                .sellerAddress(sellerSignUpReq.getSellerAddress())
                .sellerPNum(sellerSignUpReq.getSellerPNum())
                .sellerGrade(Silver)
                .sellerAuthority("Seller")
                .socialLogin(false)
                .status(false)
                .build());

        // 3. AccessToken을 생성하여
        String accessToken = JwtUtils.generateAccessToken(seller, secretKey, expiredTimeMs);

        // 4. 이메일에 포함시켜 사용자에게 전달하여 이메일 인증을 요청
        SendEmailReq sendEmailReq = SendEmailReq.builder()
                .email(seller.getSellerEmail())
                .authority(seller.getSellerAuthority())
                .accessToken(accessToken)
                .build();

        // 5. 이메일 전송
        emailService.sendEmail(sendEmailReq);

        // 6. 응답 Dto 생성을 위한 과정
        Optional<Seller> result = sellerRepository.findSellerBySellerEmail(seller.getSellerEmail());

        if (result.isPresent()) {
            seller = result.get();
        }

        // TODO : 응답 Dto 수정 필요 ! Entity의 모든 정보를 전송하고 있기때문에 Entity를 응답해주는것과 동일한 상황
        SellerSignUpRes sellerSignUpRes = SellerSignUpRes.builder()
                .sellerName(seller.getSellerName())
                .sellerEmail(seller.getSellerEmail())
                .sellerPassword(seller.getSellerPassword())
                .sellerAddress(seller.getSellerAddress())
                .sellerPNum(seller.getSellerPNum())
                .sellerGrade(seller.getSellerGrade())
                .sellerAuthority(seller.getSellerAuthority())
                .socialLogin(seller.getSocialLogin())
                .status(seller.getStatus())
                .build();

        return BaseResponse.successResponse("이메일 인증 대기중...", sellerSignUpRes);
    }

    // 단일 조회 - Read
    public BaseResponse<SellerReadRes> SellerRead(Long idx) {
        Optional<Seller> seller = sellerRepository.findById(idx);

        if (seller.isPresent()) {
            Seller sellerInfo = seller.get();
            SellerReadRes sellerReadRes = SellerReadRes
                    .builder()
                    .sellerName(sellerInfo.getSellerName())
                    .sellerEmail(sellerInfo.getSellerEmail())
                    .sellerAddress(sellerInfo.getSellerAddress())
                    .sellerPNum(sellerInfo.getSellerPNum())
                    .sellerGrade(sellerInfo.getSellerGrade())
                    .status(sellerInfo.getStatus())
                    .build();
            return BaseResponse.successResponse("요청하신 회원의 정보입니다.", sellerReadRes);
        }
        return BaseResponse.failResponse(7000, "요청하신 회원은 가입되어 있지 않습니다.");
    }

    // 다수 조회 - Read
    public BaseResponse<Object> SellerList() {
        List<Seller> sellerList = sellerRepository.findAll();

        List<SellerReadRes> sellerReadResList = new ArrayList<>();

        for (Seller seller : sellerList) {

            SellerReadRes sellerReadRes = SellerReadRes
                    .builder()
                    .sellerName(seller.getSellerName())
                    .sellerEmail(seller.getSellerEmail())
                    .sellerAddress(seller.getSellerAddress())
                    .sellerPNum(seller.getSellerPNum())
                    .sellerGrade(seller.getSellerGrade())
                    .status(seller.getStatus())
                    .build();

            sellerReadResList.add(sellerReadRes);
        }

        return BaseResponse.successResponse("요청하신 전체 회원의 정보입니다.", sellerReadResList);
    }

    // 로그인 기능
    public BaseResponse<LoginRes> SellerLogin(LoginReq sellerLoginReq) {
        LoginRes loginRes = null;
        Optional<Seller> seller = sellerRepository.findSellerBySellerEmail(sellerLoginReq.getEmail());
        if (seller.isEmpty()) {
            return BaseResponse.failResponse(7000, "가입되지 않은 회원입니다.");
        } else if (seller.isPresent() && passwordEncoder.matches(sellerLoginReq.getPassword(), seller.get().getPassword()))
            ;
        {
            loginRes = LoginRes.builder()
                    .jwtToken(JwtUtils.generateAccessToken(seller.get(), secretKey, expiredTimeMs))
                    .build();

        }
        return BaseResponse.successResponse("정상적으로 로그인 되었습니다.", loginRes);
    }

    // 회원 정보 수정 - Update
    public BaseResponse<SellerUpdateRes> SellerInfoUpdate(String email, SellerUpdateReq sellerUpdateReq) {
        Optional<Seller> seller2 = sellerRepository.findSellerBySellerEmail(email);
        if (seller2.isPresent()) {
            System.out.println("인증된 접근입니다.");
        } else throw new RuntimeException("비 인증된 접근입니다.");

        Seller seller3 = seller2.get();

        if (sellerUpdateReq.getSellerPassword() != null) {
            seller3.setSellerPassword(passwordEncoder.encode(sellerUpdateReq.getSellerPassword()));
        }
        if (sellerUpdateReq.getSellerAddress() != null) {
            seller3.setSellerAddress(sellerUpdateReq.getSellerAddress());
        }
        if (sellerUpdateReq.getSellerPNum() != null) {
            seller3.setSellerPNum(sellerUpdateReq.getSellerPNum());
        }

        Seller result = sellerRepository.save(seller3);

        SellerUpdateRes sellerUpdateRes = SellerUpdateRes.builder()
                .sellerName(result.getSellerName())
                .sellerEmail(result.getSellerEmail())
                .sellerPassword(result.getSellerPassword())
                .sellerAddress(result.getSellerAddress())
                .sellerPNum(result.getSellerPNum())
                .build();

        return BaseResponse.successResponse("요청하신 정보 수정이 완료되었습니다.", sellerUpdateRes);
    }

    //  회원 탈퇴
    public void SellerDelete(String email, String password) {
        Optional<Seller> seller2 = sellerRepository.findSellerBySellerEmail(email);
        Optional<EmailVerify> emailVerify = emailVerifyRepository.findByEmail(email);
        if (seller2.isPresent()) {
            if (emailVerify.isPresent() && passwordEncoder.matches(password, seller2.get().getSellerPassword())) {
                sellerRepository.delete(seller2.get());
                emailVerifyRepository.delete(emailVerify.get());
            }
        } else throw new RuntimeException("비 인증된 접근입니다.");


    }

    public Customer getCustomerByCustomerId(String email) {
        Optional<Customer> customer = customerRepository.findByCustomerEmail(email);

        if (customer.isPresent()) {
            return customer.get();
        }
        return null;
    }

    public Seller getSellerBySellerId(String email) {
        Optional<Seller> seller = sellerRepository.findSellerBySellerEmail(email);

        if (seller.isPresent()) {
            return seller.get();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = null;
        Seller seller = null;
        Optional<Customer> result = customerRepository.findByCustomerEmail(email);
        if (result.isPresent()) {
            customer = result.get();
            return customer;
        } else {
            Optional<Seller> result2 = sellerRepository.findSellerBySellerEmail(email);
            if (result2.isPresent()) {
                seller = result2.get();
            }
        }
        return customer;
    }
}
