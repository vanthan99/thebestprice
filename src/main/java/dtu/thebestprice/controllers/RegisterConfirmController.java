package dtu.thebestprice.controllers;

import dtu.thebestprice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registerConfirm")
public class RegisterConfirmController {
    @Autowired
    AuthService authService;

    @PostMapping
    public ResponseEntity<Object> registerConfirm(
            @RequestParam(value = "token",required = false) String token
    ){
       return authService.registerConfirm(token);
    }
}
