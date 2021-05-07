package dtu.thebestprice.controllers;

import dtu.thebestprice.entities.Subscriber;
import dtu.thebestprice.payload.request.SubscriberRequest;
import dtu.thebestprice.services.SubscriberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/subscriber")
@Api
public class SubscriberController {
    @Autowired
    SubscriberService subscriberService;

    @PostMapping("post")
    @ApiOperation(value = "Gửi email nhận thông báo")
    public ResponseEntity<Object> sendEmail(
            @RequestBody @Valid SubscriberRequest request
            ){
        return subscriberService.create(request);
    }
}
