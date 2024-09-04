package com.sparta.myselectshop.security;

import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ✅ UserDetailsImpl 클래스는 Spring Security에서 사용자 인증 정보를 제공하는 클래스입니다.
 *
 *    ➡️ User 엔티티의 정보를 바탕으로 사용자의 인증 및 권한 정보를 처리합니다.
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * ✅ UserDetailsImpl 생성자
     *
     *    ➡️ User 객체를 기반으로 사용자 인증 정보를 초기화합니다.
     *
     * @param user 인증을 위해 사용되는 사용자 정보
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * ✅ 사용자 엔티티를 반환합니다.
     *
     *    ➡️ 인증된 사용자에 대한 정보를 필요로 할 때 사용됩니다.
     *
     * @return User 사용자 엔티티 객체
     */
    public User getUser() {
        return user;
    }

    /**
     * ✅ 사용자의 비밀번호를 반환합니다.
     *
     *    ➡️ Spring Security에서 사용자 인증 시 사용됩니다.
     *
     * @return String 사용자의 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * ✅ 사용자의 이름(아이디)를 반환합니다.
     *
     *    ➡️ Spring Security에서 사용자 인증 시 사용됩니다.
     *
     * @return String 사용자의 이름(아이디)
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * ✅ 사용자의 권한을 반환합니다.
     *
     *    ➡️ 사용자가 가진 역할(Role)에 따라 권한을 설정하여 Spring Security에서 사용합니다.
     *
     * @return Collection<? extends GrantedAuthority> 사용자의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    /**
     * ✅ 계정의 만료 여부를 확인합니다.
     *
     *    ➡️ 계정이 만료되지 않았음을 나타내며, 항상 true를 반환합니다.
     *
     * @return boolean 계정의 만료 여부 (항상 true)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * ✅ 계정의 잠김 여부를 확인합니다.
     *
     *    ➡️ 계정이 잠기지 않았음을 나타내며, 항상 true를 반환합니다.
     *
     * @return boolean 계정의 잠김 여부 (항상 true)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * ✅ 자격 증명의 만료 여부를 확인합니다.
     *
     *    ➡️ 자격 증명이 만료되지 않았음을 나타내며, 항상 true를 반환합니다.
     *
     * @return boolean 자격 증명의 만료 여부 (항상 true)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * ✅ 계정의 활성화 여부를 확인합니다.
     *
     *    ➡️ 계정이 활성화되어 있음을 나타내며, 항상 true를 반환합니다.
     *
     * @return boolean 계정의 활성화 여부 (항상 true)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
