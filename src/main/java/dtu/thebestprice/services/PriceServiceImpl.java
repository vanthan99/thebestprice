package dtu.thebestprice.services;

import dtu.thebestprice.converters.PriceConverter;
import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.request.price.RetailerUpdatePriceRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.price.PriceResponse;
import dtu.thebestprice.repositories.PriceRepository;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PriceConverter priceConverter;

    @Override
    public ResponseEntity<Object> retailerUpdatePrice(long productRetailerId,RetailerUpdatePriceRequest priceRequest) {
        long price;

        try {
            price = Long.parseLong(priceRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Giá phải là số nguyên");
        }

        ProductRetailer productRetailer =
                productRetailerRepository
                        .findById(productRetailerId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại product retailer"));

        // kiểm tra xem người dùng hiện tại có phải chủ của retailer hay không
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!productRetailer.getRetailer().getUser().getUsername().equals(auth.getName()))
            throw new RuntimeException("Bạn không phải nhà bán lẽ này và không thể thay đổi giá");



        // lấy giá mới nhất
        Long latestPrice = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer).getPrice();


        // ngược lại, so sánh xem giá hiện tại và giá chuẩn bị cập nhật có bị trùng nhau không?
        if (latestPrice == price)
            throw new RuntimeException("Giá bạn muốn cập nhật và giá hiện tại không có gì thay đổi");

        // tiến hành cập nhật giá
        // thêm giá
        Price newPrice = new Price(price, productRetailer);
        priceRepository.save(newPrice);

        if (!productRetailer.getUrl().equals(priceRequest.getUrl())){
            productRetailer.setUrl(priceRequest.getUrl());
        }

        productRetailer.setApprove(false);
        productRetailerRepository.save(productRetailer);

        return ResponseEntity.ok(new ApiResponse(true, "Yêu cầu cập nhật giá của bạn đã được ghi lại. Hãy đợi quản trị viên  phê duyệt giá"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminApprovePrice(long productRetailerId) {
        ProductRetailer productRetailer =
                productRetailerRepository.findById(productRetailerId)
                        .orElseThrow(() -> new RuntimeException("Product Retailer không tồn tại"));

        if (productRetailer.isApprove())
            throw new RuntimeException("Product Retailer đã được approve giá trước đó");

        productRetailer.setApprove(true);

        productRetailerRepository.save(productRetailer);


        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt giá thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminUpdatePrice(long productRetailerId,RetailerUpdatePriceRequest priceRequest) {
        long price;

        try {
            price = Long.parseLong(priceRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Giá phải là số nguyên");
        }

        ProductRetailer productRetailer =
                productRetailerRepository
                        .findById(productRetailerId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại product retailer"));

        // lấy giá mới nhất
        Long latestPrice = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer).getPrice();


        // ngược lại, so sánh xem giá hiện tại và giá chuẩn bị cập nhật có bị trùng nhau không?
        if (latestPrice == price)
            throw new RuntimeException("Giá bạn muốn cập nhật và giá hiện tại không có gì thay đổi");

        // thêm giá
        Price newPrice = new Price(price, productRetailer);
        priceRepository.save(newPrice);

        if (!productRetailer.getUrl().equals(priceRequest.getUrl())){
            productRetailer.setUrl(priceRequest.getUrl());
        }


        productRetailer.setApprove(true);
        productRetailerRepository.save(productRetailer);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật giá thành công"));
    }

    @Override
    public ResponseEntity<Object> adminGetPriceByProduct(long productId) {
        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại sản phẩm"));

        List<PriceResponse> priceResponses =
                product.getProductRetailers()
                        .stream().map(productRetailer -> priceConverter.toPriceResponse(productRetailer)).collect(Collectors.toList());

        return ResponseEntity.ok(priceResponses);
    }
}
