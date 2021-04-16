package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.payload.response.RetailerResponse;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.services.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RetailerServiceImpl implements RetailerService {
    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    RetailerRepository retailerRepository;

    @Override
    public Set<RetailerResponse> findAll() {
        return retailerRepository.findAll().stream().map(retailer -> retailerConverter.toRetailerResponse(retailer)).collect(Collectors.toSet());
    }
}
