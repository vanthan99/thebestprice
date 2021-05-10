package dtu.thebestprice.controllers;

import dtu.thebestprice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xac-nhan-email")
public class RegisterConfirmController {
    @Autowired
    AuthService authService;

    @GetMapping
    public String registerConfirm(
            @RequestParam(value = "token",required = false) String token
    ){
       return authService.registerConfirm(token);
    }
}
