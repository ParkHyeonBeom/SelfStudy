package org.example.inflearn.Member.Model.ResDtos;


import lombok.Builder;
import lombok.Data;
import org.example.inflearn.Common.Address;

import javax.persistence.Embedded;

@Data
@Builder
public class CustomerUpdateRes {
    private String customerName;

    private String customerEmail;

    private String customerPassword;

    @Embedded
    private Address customerAddress;

    private String customerPNum;

}
