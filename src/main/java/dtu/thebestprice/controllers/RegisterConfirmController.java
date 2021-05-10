package dtu.thebestprice.controllers;

import dtu.thebestprice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registerConfirm")
public class RegisterConfirmController {
    @Autowired
    AuthService authService;

    @PostMapping
    public String registerConfirm(
            @RequestParam(value = "token",required = false) String token
    ){
       return authService.registerConfirm(token);
    }
}
