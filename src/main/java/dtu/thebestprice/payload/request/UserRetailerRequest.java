package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.Valid;

@Data
public class UserRetailerRequest {
    @Valid
    private RegisterRequest user;

    @Valid
    private RetailerRequest retailer;
}
