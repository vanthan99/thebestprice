package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.services.ProductRetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductRetailerServiceImpl implements ProductRetailerService {

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Override
    public ResponseEntity<Object> toggleEnable(long productRetailerId) {
        ProductRetailer productRetailer = productRetailerRepository
                .findById(productRetailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại product retailer"));

        String mesage;
        if (productRetailer.isEnable())
            mesage = "Tắt trạng thái hoạt động thành công";
        else mesage = "Bật trạng thái hoạt động thành công";

        productRetailer.setEnable(!productRetailer.isEnable());
        productRetailerRepository.save(productRetailer);

        return ResponseEntity.ok(new ApiResponse(true, mesage));
    }
}
