package org.example.inflearn.Member.Model.ResDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRes {

    private final String jwtToken;
}
