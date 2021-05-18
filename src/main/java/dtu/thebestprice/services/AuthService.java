package dtu.thebestprice.services;

import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.RegisterRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ApiResponse register(RegisterRequest registerRequest, ERole role);

    ResponseEntity<Object> registerConfirm(String token);


    ResponseEntity<Object> me();
}
