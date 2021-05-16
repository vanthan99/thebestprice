package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.BrandConverter;
import dtu.thebestprice.entities.Brand;
import dtu.thebestprice.payload.response.BrandResponse;
import dtu.thebestprice.repositories.BrandRepository;
import dtu.thebestprice.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandConverter brandConverter;

    @Override
    public ResponseEntity<Object> findAllBrandIsEnable() {
        List<Brand> brandListEnableTrue = brandRepository.findByDeleteFlgFalseAndEnable(true);
        List<BrandResponse> brandResponses = brandListEnableTrue
                .stream()
                .map(brand -> brandConverter.toBrandResponse(brand))
                .collect(Collectors.toList());

        return ResponseEntity.ok(brandResponses);
    }
}
