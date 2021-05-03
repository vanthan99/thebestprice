package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.RetailerResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface RetailerService {
    Set<RetailerResponse> findAll();

    ResponseEntity<Object> deleteById(String id);

    ResponseEntity<Object> create(RetailerRequest retailerRequest);

    ResponseEntity<Object> update(RetailerRequest retailerRequest, Long retailerId);

}
