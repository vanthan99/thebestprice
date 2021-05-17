package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.PriceConverter;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.price.PriceDetailResponse;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.services.ProductRetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductRetailerServiceImpl implements ProductRetailerService {

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceConverter priceConverter;

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

    @Override
    public ResponseEntity<Object> findByApprove(boolean approve, Pageable pageable) {
        Page<PriceDetailResponse> result =
                productRetailerRepository.findByDeleteFlgFalseAndApprove(approve, pageable)
                        .map(productRetailer -> priceConverter.toPriceDetailResponse(productRetailer));
        return ResponseEntity.ok(result);
    }
}
