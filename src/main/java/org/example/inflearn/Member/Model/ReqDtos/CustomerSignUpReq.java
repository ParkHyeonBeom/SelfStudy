package org.example.inflearn.Member.Model.ReqDtos;

import jakarta.persistence.Embedded;
import lombok.*;
import org.example.inflearn.Common.Address;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class CustomerSignUpReq {
    private String customerName;

    private String customerEmail;

    private String customerPassword;

    private Address customerAddress;

    private String customerPNum;

    private Boolean socialLogin;
}
