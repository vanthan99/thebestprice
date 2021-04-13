package dtu.thebestprice.securities;

import dtu.thebestprice.entities.Role;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.EUserStatusType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
public class MyUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
        });
        return authorities;
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
        return !user.isDeleteFlg() && user.getUserType().equals(EUserStatusType.ACTIVE);
    }

    public String getFullName(){
        return user.getFullName();
    }

    public String getAddress(){
        return user.getAddress();
    }

    public String getPhoneNumber(){
        return user.getPhoneNumber();
    }

    public Set<String> getRoles(){
        return user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
    }
}
