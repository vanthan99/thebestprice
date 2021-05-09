package dtu.thebestprice.securities;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@AllArgsConstructor
public class MyUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        if (!user.isEnable()) throw new RuntimeException("Tài khoản không hoạt động");
        if (!user.isApprove()) throw new RuntimeException("Tài khoản chưa được phê duyệt");
        return !user.isDeleteFlg() && user.isEnable() && user.isApprove();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public String getAddress() {
        return user.getAddress();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getRole() {
        return user.getRole().toString();
    }

    public boolean isGuest() {
        return user.getRole().equals(ERole.ROLE_GUEST);
    }

    public Long getId(){
        return user.getId();
    }

    public boolean getStatus() {
        return user.isEnable() && user.isApprove();
    }
}
