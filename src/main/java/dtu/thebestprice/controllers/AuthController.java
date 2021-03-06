package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.entities.validator.CustomValidator;
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

    @GetMapping("/me")
    @ApiOperation(value = "Th??ng tin c???a t??i")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> me() {
        return authService.me();
    }

    @PostMapping("/login")
    @ApiOperation(value = "????ng nh???p")
    public ResponseEntity<Object> login(
            @ApiParam(value = "T??i kho???n ????ng nh??p", required = true)
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        if (!CustomValidator.isValid(loginRequest.getUsername()))
            throw new RuntimeException("T??n ????ng nh???p kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");
        if (!CustomValidator.isValid(loginRequest.getPassword()))
            throw new RuntimeException("M???t kh???u kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    ));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Username ho???c password kh??ng ????ng"), HttpStatus.UNAUTHORIZED);
        }

        // N???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
        // Set th??ng tin authentication v??o Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Tr??? v??? jwt cho ng?????i d??ng.
        String jwt = jwtTokenProvider.generateToken((MyUserDetails) authentication.getPrincipal());

        template.opsForValue().set(jwt, jwt);

        return ResponseEntity.ok().body(new LoginResponse(jwt));
    }


    @PostMapping(value = "/registerGuest")
    @ApiParam(value = "????ng k?? t??i kho???n guest")
    public ResponseEntity<Object> registerGuestAccount(@RequestBody @Valid RegisterRequest registerRequest) {
        if (!CustomValidator.isValid(registerRequest.getUsername()))
            throw new RuntimeException("T??n ????ng nh???p kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");
        if (!CustomValidator.isValid(registerRequest.getPassword()))
            throw new RuntimeException("M???t kh???u kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_GUEST));
    }

    @PostMapping(value = "/registerRetailer")
    @ApiParam(value = "????ng k?? t??i kho???n Nh?? b??n l???")
    public ResponseEntity<Object> registerRetailerAccount(@RequestBody @Valid RegisterRequest registerRequest) {
        if (!CustomValidator.isValid(registerRequest.getUsername()))
            throw new RuntimeException("T??n ????ng nh???p kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");
        if (!CustomValidator.isValid(registerRequest.getPassword()))
            throw new RuntimeException("M???t kh???u kh??ng ???????c ch???a kho???ng c??ch ho???c k?? t??? ti???ng vi???t");
        return ResponseEntity.ok(authService.register(registerRequest, ERole.ROLE_RETAILER));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "????ng xu???t")
    public ResponseEntity<Object> logout(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {

        if (token == null || token.isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, "B???n ch??a ????ng ????ng nh???p v??o h??? th???ng"), HttpStatus.UNAUTHORIZED);

        if (template.hasKey(token.substring(7))) {
            template.delete(token.substring(7));
            return ResponseEntity.ok(new ApiResponse(true, "????ng xu???t th??nh c??ng"));
        } else
            return new ResponseEntity<>(new ApiResponse(false, "Token ???? h???t h???n"), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/validateToken")
    @ApiOperation(value = "Ki???m tra token")
    public ResponseEntity<Object> validateToken(
            @RequestHeader(value = "Authorization", required = false) String token,
            @AuthenticationPrincipal MyUserDetails myUserDetails
    ) {
        if (token == null)
            throw new RuntimeException("Token kh??ng t???n t???i");

        if (myUserDetails == null)
            throw new RuntimeException("Token kh??ng h???p l???");

        User user = userRepository
                .findByUsernameAndDeleteFlgFalse(myUserDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i ng?????i d??ng"));

        if (user.isDeleteFlg())
            throw new RuntimeException("T??i kho???n ???? b??? x??a");
        if (!user.isEnable() || !user.isApprove())
            throw new RuntimeException("T??i kho???n kh??ng c??n ho???t ?????ng");

        if (token == null)
            throw new RuntimeException("Kh??ng t???n t???i token");

        if (!template.hasKey(token.substring(7))) {
            throw new RuntimeException("Phi??n ????ng nh???p ???? h???t h???n");
        }

        try {
            Jwts.parser().setSigningKey("thebestprice").parseClaimsJws(token.substring(7));
            return ResponseEntity.ok().build();
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Chu???i token kh??ng h???p l???");
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Chu???i token ???? h???t h???n");
        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("JWT claims string is empty.");
        }

    }


}
