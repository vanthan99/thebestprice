package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Role;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.entities.enums.EUserStatusType;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.RoleRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.securities.JwtTokenProvider;
import dtu.thebestprice.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.hibernate.cache.spi.support.EntityReadOnlyAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ApiResponse register(RegisterRequest registerRequest, ERole eRole) {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            return new ApiResponse(false, "Tên đăng nhập đã tồn tại");
        User user = toEntity(registerRequest);

        Role role = roleRepository.findByName(eRole);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setUserType(EUserStatusType.ACTIVE);
        user.setRoles(roles);
        userRepository.save(user);
        return new ApiResponse(true, "Đăng ký thành công");
    }

    @Override
    public ApiResponse logout(String token) {
        String jwt = token.replace("Bearer ","");
        final Date now = new Date();
        final Claims claims = Jwts.parser()
                .setSigningKey(JwtTokenProvider.JWT_SECRET)
                .parseClaimsJws(jwt).getBody();
        claims.setExpiration(now);
        Jwts.builder().setClaims(claims).compact();
        return new ApiResponse(true,"Đăng xuất thành công");
    }

    private User toEntity(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setAddress(registerRequest.getAddress());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        return user;
    }
}
