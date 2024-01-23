package org.example.inflearn.Member.Model.Entity;


import lombok.*;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerIdx;

    private String sellerName;

    private String sellerEmail;

    private String sellerPassword;

    @Embedded
    private Address sellerAddress;

    private Grade sellerGrade;

    private String sellerPNum;

    private String sellerAuthority;

    private Boolean socialLogin;

    private Boolean status;

    private String businessNumber;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> sellerAuthority);
    }

    @Override
    public String getUsername() {
        return sellerEmail;
    }

    @Override
    public String getPassword()
    {
        return sellerPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
