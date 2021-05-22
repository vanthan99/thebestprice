package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.PriceConverter;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.price.ProductRetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.price.PriceDetailResponse;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.ProductRetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductRetailerServiceImpl implements ProductRetailerService {

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceConverter priceConverter;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RetailerRepository retailerRepository;

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

    @Override
    @Transactional
    public ResponseEntity<Object> update(long productRetailerId, ProductRetailerRequest productRetailerRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Hệ thống không thể nhận biết bạn là ai"));

        ProductRetailer productRetailer = productRetailerRepository.findById(productRetailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại produc retailer"));

        if (user.getRole().equals(ERole.ROLE_RETAILER) && !productRetailer.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("Bạn không phải là chủ sở hữu của nhà bán lẽ này");

        if (productRetailer.isDeleteFlg())
            throw new RuntimeException("product retailer này đã bị xóa trước đó");

        if (productRetailer.getUrl().equals(productRetailerRequest.getUrl()))
            throw new RuntimeException("Không có thay đổi mới");

        productRetailer.setUrl(productRetailerRequest.getUrl());
        if (user.getRole().equals(ERole.ROLE_ADMIN)) {
            productRetailer.setApprove(true);
            productRetailer.setEnable(true);
        } else {
            productRetailer.setApprove(false);
        }

        productRetailerRepository.save(productRetailer);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thành công"));
    }
}
