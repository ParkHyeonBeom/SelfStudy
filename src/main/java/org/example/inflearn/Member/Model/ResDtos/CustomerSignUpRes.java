package org.example.inflearn.Member.Model.ResDtos;

import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;

@Builder
@Data
public class CustomerSignUpRes {
    private String customerName;

    private String customerEmail;

    private String customerPassword;

    private Address customerAddress;

    private String customerPNum;

    private Grade customerGrade;

    private String customerAuthority;

    private Boolean socialLogin;

    private Boolean status;
}
