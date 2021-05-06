package dtu.thebestprice.services;

import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;

public interface AuthService {
    ApiResponse register(RegisterRequest registerRequest, ERole role);
}
