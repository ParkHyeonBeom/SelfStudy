package org.example.inflearn.Member.Model.Entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.example.inflearn.Common.Address;
import org.example.inflearn.Grade.Grade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements UserDetails {

    @Id
    private Long customerIdx;

    private String customerName;

    private String customerEmail;

    private String customerPassword;

    @Embedded
    private Address customerAddress;

    private String customerPNum;

    private Grade customerGrade;

    private String customerAuthority;

    private Boolean socialLogin;

    private Boolean status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> customerAuthority);
    }

    @Override
    public String getUsername() {
        return customerEmail;
    }

    @Override
    public String getPassword()
    {
        return customerPassword;
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
