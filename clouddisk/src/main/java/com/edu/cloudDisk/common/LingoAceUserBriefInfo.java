package com.edu.cloudDisk.common;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class LingoAceUserBriefInfo implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Integer role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != Constants.USER_STATUS_DELETED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Constants.USER_STATUS_FORBIDDEN;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status != Constants.USER_STATUS_INVALID;
    }

    @Override
    public boolean isEnabled() {
        return status == Constants.USER_STATUS_VALID;
    }
}
