package dtu.thebestprice.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class RootController {

    @ApiOperation(value = "Chuyển hướng đến trang tài liệu")
    @GetMapping
    public String redirect(){
        return "redirect:/swagger-ui.html";
    }
}
