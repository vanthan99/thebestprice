package dtu.thebestprice.services;

import dtu.thebestprice.payload.response.RetailerResponse;

import java.util.List;
import java.util.Set;

public interface RetailerService {
    Set<RetailerResponse> findAll();
}
