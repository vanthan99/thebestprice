package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.LoginRequest;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.LoginResponse;
import dtu.thebestprice.securities.JwtTokenProvider;
import dtu.thebestprice.securities.MyUserDetails;
import dtu.thebestprice.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    @ApiOperation(value = "Đăng nhập")
    public ResponseEntity<Object> login(
            @ApiParam(value = "Tài khoản đăng nhâp", required = true)
            @RequestBody LoginRequest loginRequest
    ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    ));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Username hoặc password không đúng"),HttpStatus.UNAUTHORIZED);
        }

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Trả về jwt cho người dùng.
        String jwt = jwtTokenProvider.generateToken((MyUserDetails) authentication.getPrincipal());

        return ResponseEntity.ok().body(new LoginResponse(jwt));
    }


    @PostMapping(value = "/registerGuest")
    @ApiParam(value = "Đăng ký tài khoản guest")
    public ResponseEntity<Object> registerGuestAccount(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_GUEST));
    }

    @PostMapping(value = "/registerRetailer")
    @ApiParam(value = "Đăng ký tài khoản Nhà bán lẽ")
    public ResponseEntity<Object> registerRetailerAccount(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_RETAILER));
    }
}
