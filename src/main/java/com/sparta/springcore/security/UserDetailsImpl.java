package com.sparta.springcore.security;

import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//회원정보 DB에서 차즌 그 유저네임으로 조회된 해당 회원의 객체
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    // DB에서 가져온 Userdetails랑 로그인 시오한거랑 일치하는지 비교할때 필요
    // 갖고 있던 멤버변수인 유저레퍼지토리로 받은 user에서 패스워드와 유저네임을 빼서 정보 전달하겠다
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    // DB에서 가져온 Userdetails랑 로그인 시오한거랑 일치하는지 비교할때 필요
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    //여기 아래부터는 설명 안해줌;;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }
}