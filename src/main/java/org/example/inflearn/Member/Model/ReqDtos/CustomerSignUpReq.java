package org.example.inflearn.Member.Model.ReqDtos;

import lombok.*;
import org.example.inflearn.Common.Address;

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
