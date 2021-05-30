package dtu.thebestprice.services;

import dtu.thebestprice.converters.PriceConverter;
import dtu.thebestprice.entities.*;
import dtu.thebestprice.payload.request.price.PriceRequest;
import dtu.thebestprice.payload.request.price.PriceRetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.price.PriceResponse;
import dtu.thebestprice.repositories.*;
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

    @Autowired
    RetailerRepository retailerRepository;

    @Override
    public ResponseEntity<Object> retailerUpdatePrice(long productRetailerId, PriceRequest priceRequest) {
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

        if (!productRetailer.getCreatedBy().equals(auth.getName()))
            throw new RuntimeException("Bạn không phải nhà bán lẽ này và không thể thay đổi giá");


        // lấy giá mới nhất
        Long latestPrice = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer).getPrice();


        // ngược lại, so sánh xem giá hiện tại và giá chuẩn bị cập nhật có bị trùng nhau không?
        if (latestPrice == price)
            throw new RuntimeException("Giá bạn muốn cập nhật và giá hiện tại không có gì thay đổi");

        if (productRetailerRepository.existsByDeleteFlgFalseAndUrl(priceRequest.getUrl()) && !productRetailer.getUrl().equals(priceRequest.getUrl()))
            throw new RuntimeException("URL đã bị trùng");

        // tiến hành cập nhật giá
        // thêm giá
        Price newPrice = new Price(price, productRetailer);
        priceRepository.save(newPrice);

        if (!productRetailer.getUrl().equals(priceRequest.getUrl())) {
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
        productRetailer.setEnable(true);

        productRetailerRepository.save(productRetailer);


        return ResponseEntity.ok(new ApiResponse(true, "Phê duyệt giá thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminUpdatePrice(long productRetailerId, PriceRequest priceRequest) {
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

        if (productRetailerRepository.existsByDeleteFlgFalseAndUrl(priceRequest.getUrl()) && !productRetailer.getUrl().equals(priceRequest.getUrl()))
            throw new RuntimeException("URL đã bị trùng");

        // thêm giá
        Price newPrice = new Price(price, productRetailer);
        priceRepository.save(newPrice);

        if (!productRetailer.getUrl().equals(priceRequest.getUrl())) {
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
                productRetailerRepository.findByDeleteFlgFalseAndProduct(product)
                        .stream().map(productRetailer -> priceConverter.toPriceResponse(productRetailer)).collect(Collectors.toList());

        return ResponseEntity.ok(priceResponses);
    }

    @Override
    public ResponseEntity<Object> retailerCreateNewPrice(long productId, PriceRetailerRequest priceRetailerRequest) {
        long retailerId;
        try {
            retailerId = Long.parseLong(priceRetailerRequest.getRetailerId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của nhà bán lẽ phải là số nguyên");
        }

        long priceLong;
        try {
            priceLong = Long.parseLong(priceRetailerRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Giá phải là số nguyên");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại product"));

        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà bán lẽ"));

        if (!retailer.getUser().getUsername().equals(authentication.getName()))
            throw new RuntimeException("Bạn không phải là chủ của nhà bán lẽ này");

        if (retailer.isDeleteFlg())
            throw new RuntimeException("Nhà bán lẽ này đã bị xóa");

        if (!retailer.isEnable())
            throw new RuntimeException("Nhà bán lẽ này đã bị khóa");

        if (product.isDeleteFlg())
            throw new RuntimeException("Sản phẩm đã bị xóa");

        if (!product.isEnable())
            throw new RuntimeException("Sản phẩm đã bị khóa");

        if (productRetailerRepository.existsByProductAndRetailerAndDeleteFlgFalse(product, retailer))
            throw new RuntimeException("Nhà bán lẽ này đã kinh doanh sản phẩm trước đó");

        if (productRetailerRepository.existsByDeleteFlgFalseAndUrl(priceRetailerRequest.getUrl()))
            throw new RuntimeException("URL đã bị trùng");

        ProductRetailer productRetailer = new ProductRetailer(
                priceRetailerRequest.getUrl(),
                retailer,
                product,
                false,
                false
        );

        productRetailerRepository.save(productRetailer);


        Price price = new Price(priceLong, productRetailer);
        priceRepository.save(price);

        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới giá thành công. hãy đợi admin phê duyệt"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> retailerDelete(long productRetailerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ProductRetailer productRetailer =
                productRetailerRepository.findById(productRetailerId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại product retailer"));

        if (!productRetailer.getCreatedBy().equals(authentication.getName()))
            throw new RuntimeException("bạn không có quyền xóa product retailer này");

        if (productRetailer.isDeleteFlg())
            throw new RuntimeException("product retailer đã bị xóa trước đó");

        productRetailer.setDeleteFlg(true);
        productRetailerRepository.save(productRetailer);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminCreateNewPrice(long productId, PriceRetailerRequest priceRequest) {
        long priceLong;
        try {
            priceLong = Long.parseLong(priceRequest.getPrice());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Giá phải là số nguyên");
        }
        long retailerId;
        try {
            retailerId = Long.parseLong(priceRequest.getRetailerId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Id của nhà bán lẽ phải là số nguyên");
        }


        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại product"));

        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà bán lẽ"));

        if (retailer.isDeleteFlg())
            throw new RuntimeException("Nhà bán lẽ này đã bị xóa");

        if (!retailer.isEnable())
            throw new RuntimeException("Nhà bán lẽ này đã bị khóa");

        if (product.isDeleteFlg())
            throw new RuntimeException("Sản phẩm đã bị xóa");

        if (!product.isEnable())
            throw new RuntimeException("Sản phẩm đã bị khóa");

        if (productRetailerRepository.existsByProductAndRetailerAndDeleteFlgFalse(product, retailer))
            throw new RuntimeException("Nhà bán lẽ này đã kinh doanh sản phẩm trước đó");

        if (productRetailerRepository.existsByDeleteFlgFalseAndUrl(priceRequest.getUrl()))
            throw new RuntimeException("URL đã bị trùng");

        ProductRetailer productRetailer = new ProductRetailer(
                priceRequest.getUrl(),
                retailer,
                product,
                true,
                true
        );

        productRetailerRepository.save(productRetailer);


        Price price = new Price(priceLong, productRetailer);
        priceRepository.save(price);

        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới giá thành công."));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminDelete(long productRetailerId) {
        ProductRetailer productRetailer =
                productRetailerRepository.findById(productRetailerId)
                        .orElseThrow(() -> new RuntimeException("Không tồn tại product retailer"));

        if (productRetailer.isDeleteFlg())
            throw new RuntimeException("product retailer đã bị xóa trước đó");

        productRetailer.setDeleteFlg(true);
        productRetailerRepository.save(productRetailer);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa thành công"));
    }
}
