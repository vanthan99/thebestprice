package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.ViewCountStatistic;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.repositories.ViewCountStatisticRepository;
import dtu.thebestprice.services.ProductTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class ProductTrackingServiceImpl implements ProductTrackingService {

    @Autowired
    ViewCountStatisticRepository viewCountStatisticRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<Object> productTracking(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Id sản phẩn không tồn tại");
        }

        Product product = productRepository.getOne(productId);
        if (product.getCreatedBy().equals(authentication.getName()))
            return ResponseEntity.ok().build();

        /*
         * Cập nhật viewcount trong product lên 1 đơn vị
         * */
        productRepository.updateViewCount(productId);

        /*
         * Cập nhật bảng View Count Statistic
         *
         * */
        LocalDate nowDate = LocalDate.now();

        if (viewCountStatisticRepository.existsByStatisticDayAndProduct(nowDate, product)) {
            // Cập nhật lên 1 đơn vị
            ViewCountStatistic viewCountStatistic = viewCountStatisticRepository.findByStatisticDayAndProduct(nowDate, product);

            viewCountStatistic.setViewCount(viewCountStatistic.getViewCount() + 1);

            viewCountStatisticRepository.save(viewCountStatistic);
        }

        /*
         * Ngược lại nếu ngày hôm nay chưa có thống kê nào thì tạo mới bảng thống kê của ngày hôm nay.
         * */
        else {
            ViewCountStatistic viewCountStatistic = new ViewCountStatistic();
            viewCountStatistic.setProduct(product);
            viewCountStatistic.setStatisticDay(nowDate);
            viewCountStatistic.setViewCount(1L);

            viewCountStatisticRepository.save(viewCountStatistic);
        }


        return ResponseEntity.ok(new ApiResponse(true, "Tracking thành công"));
    }
}
