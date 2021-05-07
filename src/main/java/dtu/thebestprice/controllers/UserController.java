package dtu.thebestprice.controllers;

import dtu.thebestprice.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/approveRetailer")
    @ApiOperation(value = "Phê duyệt tài khoản retailer")
    public ResponseEntity<Object> approveRetailerAccount(
            @RequestParam(value = "id",required = false) String strRetailerId
    ){
        Long retailerId = null;
        try {
            retailerId = Long.parseLong(strRetailerId);
        }catch (Exception e){
            throw new RuntimeException("Id không đúng định dạng");
        }

        return userService.approveRetailerAccount(retailerId);

    }

    @GetMapping("/listRetailerAccountApproveFalse")
    @ApiOperation(value = " Danh sách những tài khoản nhà bán lẽ đợi phê duyệt")
    public ResponseEntity<Object> listRetailerAccountApproveFalse(Pageable pageable){
        return userService.listAccountApproveFalse(pageable);
    }

}
