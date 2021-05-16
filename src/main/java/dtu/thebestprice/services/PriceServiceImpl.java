package dtu.thebestprice.services;

import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.payload.request.price.RetailerUpdatePriceRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.PriceRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<Object> retailerUpdatePrice(RetailerUpdatePriceRequest priceRequest) {
        long productRetailerId;
        long price;

        try {
            productRetailerId = Long.parseLong(priceRequest.getProductRetailerId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id của product retailer phải là số nguyên");
        }

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
        Long latestPrice = priceRepository.findByPriceLatestByProductRetailer(productRetailer);

        // nếu giá mới nhất là null thì tạo mới 1 giá
        if (latestPrice == null) {
            Price newPrice = new Price(price, productRetailer, false, false);
            priceRepository.save(newPrice);
        } else {
            // ngược lại, so sánh xem giá hiện tại và giá chuẩn bị cập nhật có bị trùng nhau không?
            if (latestPrice == price)
                throw new RuntimeException("Giá bạn muốn cập nhật và giá hiện tại không có gì thay đổi");

            // tiến hành cập nhật giá
            // thêm giá
            Price newPrice = new Price(price, productRetailer, false, false);
            priceRepository.save(newPrice);
        }
        return ResponseEntity.ok(new ApiResponse(true, "Yêu cầu cập nhật giá của bạn đã được ghi lại. Hãy đợi quản trị viên  phê duyệt giá"));
    }

    @Override
    public ResponseEntity<Object> adminApprovePrice(long priceId) {
        Price price = priceRepository
                .findById(priceId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại id giá"));

        // đưa giá trước đó về trạng thái active false
        Price oldPrice = priceRepository
                .findByProductRetailerAndActive(price.getProductRetailer(), true);

        if (oldPrice != null) {
            oldPrice.setActive(false);
            priceRepository.save(oldPrice);
        }

        // approve giá
        price.setApprove(true);
        price.setActive(true);
        priceRepository.save(price);

        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt giá thành công"));
    }

    @Override
    public ResponseEntity<Object> adminUpdatePrice(RetailerUpdatePriceRequest priceRequest) {
        long productRetailerId;
        long price;

        try {
            productRetailerId = Long.parseLong(priceRequest.getProductRetailerId());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id của product retailer phải là số nguyên");
        }

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
        Long latestPrice = priceRepository.findByPriceLatestByProductRetailer(productRetailer);

        // nếu giá mới nhất là null thì tạo mới 1 giá
        if (latestPrice == null) {
            Price newPrice = new Price(price, productRetailer, true, true);
            priceRepository.save(newPrice);
        } else {
            // ngược lại, so sánh xem giá hiện tại và giá chuẩn bị cập nhật có bị trùng nhau không?
            if (latestPrice == price)
                throw new RuntimeException("Giá bạn muốn cập nhật và giá hiện tại không có gì thay đổi");

            // tiến hành cập nhật giá
            // đưa giá trước đó về trạng thái active false
            Price oldPrice = priceRepository
                    .findByProductRetailerAndActive(productRetailer, true);

            if (oldPrice != null) {
                oldPrice.setActive(false);
                priceRepository.save(oldPrice);
            }

            // thêm giá
            Price newPrice = new Price(price, productRetailer, true, true);
            priceRepository.save(newPrice);
        }
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật giá thành công"));
    }
}
