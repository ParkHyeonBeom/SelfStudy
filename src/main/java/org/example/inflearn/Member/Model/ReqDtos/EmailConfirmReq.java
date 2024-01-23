package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailConfirmReq {
    private String email;
    private String token;
    private String jwt;
    private String authority;
}
