package org.hkurh.doky.security;

import java.util.Collection;
import java.util.Collections;

import org.hkurh.doky.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DokyUserDetails implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static DokyUserDetails createUserDetails(final UserEntity userEntity) {
        var dokyUserDetails = new DokyUserDetails();
        dokyUserDetails.username = userEntity.getUid();
        dokyUserDetails.password = userEntity.getPassword();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        dokyUserDetails.grantedAuthorities = Collections.singletonList(grantedAuthority);

        return dokyUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
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
