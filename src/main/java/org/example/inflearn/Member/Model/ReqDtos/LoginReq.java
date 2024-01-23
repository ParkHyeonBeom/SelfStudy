package org.example.inflearn.Member.Model.ReqDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReq {
    private final String email;
    private final String password;
}
