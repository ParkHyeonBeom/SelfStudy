package org.example.inflearn.Email.Service;

import lombok.RequiredArgsConstructor;
import org.example.inflearn.Email.Model.EmailVerify;
import org.example.inflearn.Email.Model.SendEmailReq;
import org.example.inflearn.Email.Repository.EmailVerifyRepository;
import org.example.inflearn.Member.Model.Entity.Customer;
import org.example.inflearn.Member.Model.Entity.Seller;
import org.example.inflearn.Member.Model.ReqDtos.EmailConfirmReq;
import org.example.inflearn.Member.Repository.CustomerRepository;
import org.example.inflearn.Member.Repository.SellerRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final EmailVerifyRepository emailVerifyRepository;

    private final CustomerRepository customerRepository;

    private final SellerRepository sellerRepository;

    // 이메일 전송 메소드
    public void sendEmail(SendEmailReq sendEmailReq) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendEmailReq.getEmail());
        message.setSubject("[혼자 만들어보는 백엔드] 이메일 인증");

        // UUID도 생성하여 추가적으로 메일 전송
        String uuid = UUID.randomUUID().toString();
        message.setText("http://localhost:8080/customer/confirm?email="
                + sendEmailReq.getEmail()
                + "&authority=" + sendEmailReq.getAuthority()
                + "&token=" + uuid
                + "&jwt=" + sendEmailReq.getAccessToken()
        );
        emailSender.send(message);
        create(sendEmailReq.getEmail(),uuid,sendEmailReq.getAccessToken());
    }

    // 이메일 전송 후 인증 여부를 저장하기 위한 메소드
    public void create(String email,String uuid, String AccessToken)
    {
        EmailVerify emailVerify = EmailVerify.builder()
                .email(email)
                .uuid(uuid)
                .jwt(AccessToken)
                .build();
        emailVerifyRepository.save(emailVerify);
    }

    // 이메일로 전송된 링크를 검증하기 위한 메소드
    public RedirectView verify(EmailConfirmReq emailConfirmReq) {
        Optional<EmailVerify> result = emailVerifyRepository.findByEmail(emailConfirmReq.getEmail());
        if(result.isPresent()){
            EmailVerify emailVerify = result.get();
            if(emailVerify.getJwt().equals(emailConfirmReq.getJwt()) && emailVerify.getUuid().equals(emailConfirmReq.getToken())) {
                update(emailConfirmReq.getEmail(), emailConfirmReq.getAuthority());
                return new RedirectView("http://localhost:3000/emailconfirm/" + emailConfirmReq.getEmail()+emailConfirmReq.getToken()+emailConfirmReq.getJwt());
            }
        }
        return new RedirectView("http://localhost:3000/emailCertError");
    }


    // 검증된 사용자의 status를 변경하기 위한 메소드
    public void update(String email, String authority) {
        if (authority.equals("Customer")){
            Optional<Customer> result = customerRepository.findByCustomerEmail(email);
            if(result.isPresent()) {
                Customer customer = result.get();
                customer.setStatus(true);
                customerRepository.save(customer);
            }
        }else if (authority.equals("Seller")){
            Optional<Seller> result = sellerRepository.findSellerBySellerEmail(email);
            if(result.isPresent()) {
                Seller seller = result.get();
                seller.setStatus(true);
                sellerRepository.save(seller);
            }
        }
    }


}
