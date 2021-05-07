package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.LoginRequest;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.LoginResponse;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.securities.JwtTokenProvider;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@Api
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisTemplate template;

//    @PostConstruct
//    public void initUser(){
//        authService.register(new RegisterRequest(
//                "truongvanthan",
//                "thanthan",
//                "Trương Văn Thân",
//                "vanthan.ad.it@gmail.com",
//                "Triệu Phong - Quảng Trị",
//                "0365843463"
//        ), ERole.ROLE_ADMIN);
//
//    }

    @PostMapping("/login")
    @ApiOperation(value = "Đăng nhập")
    public ResponseEntity<Object> login(
            @ApiParam(value = "Tài khoản đăng nhâp", required = true)
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    ));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Username hoặc password không đúng"), HttpStatus.UNAUTHORIZED);
        }

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Trả về jwt cho người dùng.
        String jwt = jwtTokenProvider.generateToken((MyUserDetails) authentication.getPrincipal());

        template.opsForValue().set(jwt, jwt);

        return ResponseEntity.ok().body(new LoginResponse(jwt));
    }


    @PostMapping(value = "/registerGuest")
    @ApiParam(value = "Đăng ký tài khoản guest")
    public ResponseEntity<Object> registerGuestAccount(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_GUEST));
    }

    @PostMapping(value = "/registerRetailer")
    @ApiParam(value = "Đăng ký tài khoản Nhà bán lẽ")
    public ResponseEntity<Object> registerRetailerAccount(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_RETAILER));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_RETAILER','ROLE_GUEST')")
    @ApiOperation(value = "Đăng xuất")
    public ResponseEntity<Object> logout(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {

        if (template.hasKey(token.substring(7))) {
            template.delete(token.substring(7));
            return ResponseEntity.ok(new ApiResponse(true, "Đăng xuất thành công"));
        } else
            return new ResponseEntity<>(new ApiResponse(false, "Bạn chưa đăng đăng nhập vào hệ thống"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/validateToken")
    @ApiOperation(value = "Kiểm tra token")
    public ResponseEntity<Object> validateToken(
            @RequestHeader(value = "Authorization", required = false) String token,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        if (token == null)
            throw new RuntimeException("Token không tồn tại");

        if (myUserDetails == null)
            throw new RuntimeException("Token không hợp lệ");

        User user = userRepository
                .findByUsername(myUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tồn tại người dùng"));

        if (user.isDeleteFlg())
            throw new RuntimeException("Tài khoản đã bị xóa");
        if (!user.isEnable() || !user.isApprove())
            throw new RuntimeException("Tài khoản không còn hoạt động");

        if (token == null)
            throw new RuntimeException("Không tồn tại token");

        if (!template.hasKey(token.substring(7))) {
            throw new RuntimeException("Phiên đăng nhập đã hết hạn");
        }

        try {
            Jwts.parser().setSigningKey("thebestprice").parseClaimsJws(token.substring(7));
            return ResponseEntity.ok().build();
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Chuỗi token không hợp lệ");
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Chuỗi token đã hết hạn");
        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("JWT claims string is empty.");
        }

    }


}
